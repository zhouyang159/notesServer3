package com.aboutdk.note.controller;

import com.aboutdk.note.POJO.FORM.NoteForm;
import com.aboutdk.note.POJO.DO.NoteDO;
import com.aboutdk.note.POJO.VO.ResponseVO;
import com.aboutdk.note.exception.UpdateNoteVersionWrongException;
import com.aboutdk.note.mapper.mapStructMapper.NoteDOMapper;
import com.aboutdk.note.mapper.mybatisMapper.NoteMapper;
import com.aboutdk.note.security.TokenService;
import com.aboutdk.note.POJO.VO.NoteVO;
import com.aboutdk.note.service.INoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.aboutdk.note.consts.NoteConst.DELETED_NOTE;

/**
 * @author zhouyang
 * @date 2022/10/01
 */
@RestController
@RequestMapping("/note")
@Slf4j
public class NoteController extends Thread {

    @Autowired
    private INoteService noteService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private NoteMapper noteMapper;


    @GetMapping("/findAll")
    public ResponseVO<List<NoteVO>> findAll(@RequestHeader HttpHeaders headers) {
        log.info("fetch notes");

        List<NoteDO> noteDOList;
        String username = tokenService.getUsername(headers.getFirst("token"));
        noteDOList = noteService.findAllByUsername(username);

        List<NoteVO> noteVoList = noteDOList
                .stream()
                .map((note) -> {
                    NoteVO noteVO = NoteDOMapper.INSTANCE.noteDOToNoteVO(note);
                    // this findAll api didn't return content data
                    // noteVO.setContent(null);
                    return noteVO;
                })
                .collect(Collectors.toList());

        return ResponseVO.success(noteVoList);
    }

    @GetMapping("/{id}")
    public ResponseVO<NoteDO> findById(@PathVariable String id,
                                       @RequestHeader HttpHeaders headers) throws Exception {
        log.info("fetch notes");
        String username = tokenService.getUsername(headers.getFirst("token"));

        NoteDO noteDO = noteMapper.selectById(id);
        if (noteDO == null) {
            throw new RuntimeException("id not found");
        }
        if (!noteDO.getUsername().equals(username)) {
            throw new RuntimeException("this note is not belong to you");
        }

        return ResponseVO.success(noteDO);
    }

    @PostMapping
    public ResponseVO addNote(@Valid @RequestBody NoteForm newNote,
                              @RequestHeader HttpHeaders headers) {
        log.info("addNote: {}", newNote.toString());
        String curUsername = tokenService.getUsername(headers.getFirst("token"));

        NoteDO noteDO = noteService.addNote(curUsername, newNote);

        return ResponseVO.success(noteDO);
    }

    @PutMapping
    public ResponseVO updateNoteAndReorder(@Valid @RequestBody NoteForm updateForm,
                                           @RequestHeader HttpHeaders headers) {
        if (updateForm.getDeleted().equals(DELETED_NOTE)) {
            throw new RuntimeException("此接口不允许伪删除note");
        }

        String curUser = tokenService.getUsername(headers.getFirst("token"));

        // update old note's number
        List<NoteDO> allNoteListDO = noteService.findAllByUsername(curUser);
        List<NoteDO> liveNoteList = allNoteListDO
                .stream()
                .filter((note) -> {
                    return !note.getDeleted().equals(DELETED_NOTE);
                })
                .filter((note) -> {
                    if (!Objects.equals(note.getId(), updateForm.getId())) {
                        return true;
                    }
                    return false;
                })
                .sorted(new Comparator<NoteDO>() {
                    @Override
                    public int compare(NoteDO o1, NoteDO o2) {
                        return o1.getNumber() - o2.getNumber();
                    }
                })
                .collect(Collectors.toList());

        for (int i = 0; i < liveNoteList.size(); i++) {
            NoteDO cur = liveNoteList.get(i);
            cur.setNumber(i + 1);
        }

        // set the updateForm to number 0, so it can be shown in first of the note list
        updateForm.setNumber(0);
        noteService.updateNote(curUser, updateForm, true);
        noteService.updateBatchById(liveNoteList);

        List<NoteVO> res = this.findLiveListByUsername(curUser);
        return ResponseVO.success(res);
    }

    /*
     *  此接口只提供修改 liveNoteList 排序的能力，也就是只修改 number 字段，不修改其他字段
     */
    @PutMapping("/updateLiveNoteList")
    public ResponseVO updateLiveNoteList(@RequestBody List<NoteForm> formList,
                                         @RequestHeader HttpHeaders headers) throws Exception {
        // validate formList number
        for (int i = 0; i < formList.size(); i++) {
            NoteForm cur = formList.get(i);
            if (cur.getNumber() == i) {
                continue;
            } else {
                throw new RuntimeException("number排序不正确, 写入失败");
            }
        }

        String curUser = tokenService.getUsername(headers.getFirst("token"));
        List<NoteVO> liveNoteVoList = findLiveListByUsername(curUser);

        for (int i = 0; i < liveNoteVoList.size(); i++) {
            NoteVO cur = liveNoteVoList.get(i);
            boolean find = false;
            for (int j = 0; j < formList.size(); j++) {
                NoteForm noteForm = formList.get(j);
                if (cur.getId().equals(noteForm.getId())) {
                    int number = noteForm.getNumber();
                    cur.setNumber(number);

                    find = true;
                    break;
                }
            }

            if (find == false) {
                throw new Exception("排序时未找到相对应note");
            }
        }

        for (int i = 0; i < liveNoteVoList.size(); i++) {
            NoteVO update = liveNoteVoList.get(i);
            NoteForm form = new NoteForm();
            BeanUtils.copyProperties(update, form);
            noteService.updateNote(curUser, form);
        }

        List<NoteVO> res = this.findLiveListByUsername(curUser);
        return ResponseVO.success(res);
    }

    @DeleteMapping("/toTrash/{fakeDeleteId}")
    public ResponseVO fakeDelete(@PathVariable String fakeDeleteId,
                                 @RequestHeader HttpHeaders headers) {
        log.info("fake delete note: {}", fakeDeleteId);
        String curUser = tokenService.getUsername(headers.getFirst("token"));

        NoteForm find = null;
        List<NoteVO> liveNoteVoList = findLiveListByUsername(curUser);
        for (NoteVO cur : liveNoteVoList) {
            if (cur.getId().equals(fakeDeleteId)) {
                NoteForm form = new NoteForm();
                BeanUtils.copyProperties(cur, form);
                find = form;
                break;
            }
        }
        if (find == null) {
            throw new RuntimeException("not find live note with id: " + fakeDeleteId);
        }

        final Integer[] idx = {0};
        List<NoteForm> noteFormList = liveNoteVoList.stream()
                .filter((note) -> {
                    return !note.getId().equals(fakeDeleteId);
                })
                .peek((note) -> {
                    note.setNumber(idx[0]);
                    idx[0] = idx[0] + 1;
                })
                .map((noteVO) -> {
                    NoteForm form = new NoteForm();
                    BeanUtils.copyProperties(noteVO, form);
                    return form;
                })
                .collect(Collectors.toList());

        for (NoteForm noteForm : noteFormList) {
            noteService.updateNote(curUser, noteForm);
        }
        find.setNumber(-1);
        find.setDeleted(1);
        noteService.updateNote(curUser, find);

        List<NoteVO> res = this.findLiveListByUsername(curUser);
        return ResponseVO.success(res);
    }

    @DeleteMapping("/{noteId}")
    public ResponseVO delete(@PathVariable String noteId,
                             @RequestHeader HttpHeaders headers) {
        log.info("delete note: {}", noteId);
        String curUser = tokenService.getUsername(headers.getFirst("token"));
        NoteDO noteDO = noteService.deleteNote(curUser, noteId);
        return ResponseVO.success(noteDO);
    }

    public List<NoteVO> findLiveListByUsername(String username) {

        List<NoteVO> liveNoteFormList = noteService.findAllByUsername(username)
                .stream()
                .filter((note) -> !note.getDeleted().equals(DELETED_NOTE))
                .map((note) -> {
                    NoteVO noteVO = NoteDOMapper.INSTANCE.noteDOToNoteVO(note);
                    return noteVO;
                })
                .sorted(new Comparator<NoteVO>() {
                    @Override
                    public int compare(NoteVO o1, NoteVO o2) {
                        return o1.getNumber() - o2.getNumber();
                    }
                })
                .collect(Collectors.toList());

        return liveNoteFormList;
    }
}
