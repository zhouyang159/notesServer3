package com.aboutdk.note.service.impl;

import com.aboutdk.note.POJO.DO.NoteDO;
import com.aboutdk.note.POJO.FORM.NoteForm;
import com.aboutdk.note.mapper.mybatisMapper.NoteMapper;
import com.aboutdk.note.service.INoteService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.aboutdk.note.consts.NoteConst.DELETED_NOTE;
import static com.aboutdk.note.consts.NoteConst.LIVE_NOTE;

@Service
@Slf4j
public class NoteServiceImpl implements INoteService {

   @Autowired
   private NoteMapper noteMapper;

   @Override
   public List<NoteDO> findAllByUsername(String username) {
      QueryWrapper<NoteDO> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("username", username);

      List<NoteDO> noteDOList = noteMapper.selectList(queryWrapper)
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
   public NoteDO findById(String id) {
      return noteMapper.selectById(id);
   }

   @Override
   public NoteDO addNote(String username, NoteForm form) {
      NoteDO newNoteDO = new NoteDO();
      BeanUtils.copyProperties(form, newNoteDO);
      newNoteDO.setUsername(username);
      newNoteDO.setUpdateTime(LocalDateTime.now());

      noteMapper.insert(newNoteDO);

      return noteMapper.selectById(newNoteDO.getId());
   }

   @Override
   public NoteDO updateNote(String username, NoteForm form) {
      return this.realUpdateNote(username, form, false);
   }

   @Override
   public NoteDO updateNote(String username, NoteForm form, Boolean refreshUpdateTime) {
      return this.realUpdateNote(username, form, refreshUpdateTime);
   }

   public NoteDO realUpdateNote(String username, NoteForm form, Boolean refreshUpdateTime) {
      this.validateNote(username, form.getId());

      NoteDO update = this.findById(form.getId());
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
      noteMapper.updateById(update);

      return noteMapper.selectById(update.getId());
   }

   @Override
   public NoteDO deleteNote(String username, String noteId) {
      this.validateNote(username, noteId);

      NoteDO deleteNoteDO = noteMapper.selectById(noteId);
      deleteNoteDO.setDeleteForever(true);
      noteMapper.updateById(deleteNoteDO);

      return deleteNoteDO;
   }

   private void validateNote(String username, String noteId) {

      NoteDO noteDO = noteMapper.selectById(noteId);
      if (noteDO == null) {
         throw new RuntimeException("note id not exist");
      }
      if (!noteDO.getUsername().equals(username)) {
         throw new RuntimeException("note with id " + noteId + " is not belong to " + username);
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
