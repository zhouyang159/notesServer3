package com.aboutdk.note.service.impl;

import com.aboutdk.note.POJO.DO.NoteDO;
import com.aboutdk.note.POJO.FORM.NoteForm;
import com.aboutdk.note.mapper.mybatisMapper.NoteMapper;
import com.aboutdk.note.service.INoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.aboutdk.note.consts.NoteConst.DELETED_NOTE;
import static com.aboutdk.note.consts.NoteConst.LIVE_NOTE;

@Service
@Slf4j
public class NoteServiceImpl extends ServiceImpl<NoteMapper, NoteDO> implements INoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public List<NoteDO> findAllByUsername(String username) {
        List<NoteDO> noteDOList = lambdaQuery()
                .eq(NoteDO::getUsername, username)
                .list()
                .stream()
                .filter((item) -> {
                    if (item.getDeleteForever() == false) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        return noteDOList;
    }

    @Override
    @Transactional
    public NoteDO addNote(String username, NoteForm form) {
        // update all note's number first
        List<NoteDO> allNoteListDO = this.findAllByUsername(username);

        List<NoteDO> liveNoteFormList = allNoteListDO
                .stream()
                .filter((item) -> {
                    if (item.getDeleted().equals(LIVE_NOTE)) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .peek(note -> note.setNumber(note.getNumber() + 1))
                .collect(Collectors.toList());

        CompletableFuture<Boolean> updateFuture = CompletableFuture.supplyAsync(() ->
                this.updateBatchById(liveNoteFormList)
        );

        updateFuture.thenAccept((result) -> {
            if (!result) {
                throw new RuntimeException("update note number failed");
            }
        });

        NoteDO newNoteDO = new NoteDO();
        BeanUtils.copyProperties(form, newNoteDO);
        newNoteDO.setUsername(username);
        newNoteDO.setUpdateTime(LocalDateTime.now());

        noteMapper.insert(newNoteDO);
        return noteMapper.selectById(newNoteDO.getId());
    }

    @Override
    public NoteDO updateNote(String username,
                             NoteForm form) {
        return this.realUpdateNote(username, form, false);
    }

    @Override
    public NoteDO updateNote(String username,
                             NoteForm form,
                             Boolean refreshUpdateTime) {
        return this.realUpdateNote(username, form, refreshUpdateTime);
    }

    public NoteDO realUpdateNote(String username,
                                 NoteForm form,
                                 Boolean refreshUpdateTime) {
        NoteDO update = noteMapper.selectById(form.getId());
        this.validateNote(username, update);

        BeanUtils.copyProperties(form, update);

        if (update.getDeleted().equals(DELETED_NOTE)) {
            // fake delete note, we set delete_time to now and set number to -1
            update.setDeleteTime(LocalDateTime.now());
            update.setNumber(-1);
        } else if (update.getDeleted().equals(LIVE_NOTE)) {
            update.setDeleteTime(null);
        }

        if (refreshUpdateTime) {
            update.setUpdateTime(LocalDateTime.now());
        }

        int rowsAffected = noteMapper.updateById(update);
        if (rowsAffected == 0) {
            throw new RuntimeException("Update failed for note with id " + update.getId());
        }

        return update;
    }

    @Override
    public NoteDO deleteNote(String username, String noteId) {
        NoteDO deleteNoteDO = noteMapper.selectById(noteId);
        this.validateNote(username, deleteNoteDO);

        deleteNoteDO.setDeleteForever(true);
        noteMapper.updateById(deleteNoteDO);

        return deleteNoteDO;
    }

    private void validateNote(String username, NoteDO noteDO) {
        if (noteDO == null) {
            throw new RuntimeException("note id not exist");
        }
        if (!noteDO.getUsername().equals(username)) {
            throw new RuntimeException("note with id " + noteDO.getId() + " is not belong to " + username);
        }
    }

    public void deleteNoteRegular() {
        log.info("=================== begin delete notes ===================");

        List<NoteDO> allNotes = noteMapper.selectList(null);
        allNotes.forEach((noteDO) -> {
            log.warn(noteDO.toString());

            if (noteDO.getDeleted().equals(DELETED_NOTE)) {
                LocalDateTime now = LocalDateTime.now();

                int diffDay = now.compareTo(noteDO.getUpdateTime());
                long period = 5;
                if (diffDay > period) {
                    // remove old deleted note
                    log.info("delete this note forever: {}", noteDO);

                    NoteDO deleteNoteDO = noteMapper.selectById(noteDO.getId());
                    deleteNoteDO.setDeleteForever(true);

                    noteMapper.updateById(deleteNoteDO);
                }
            }
        });
    }

}
