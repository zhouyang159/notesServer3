package com.aboutdk.note.service;

import com.aboutdk.note.POJO.DO.NoteDO;
import com.aboutdk.note.POJO.FORM.NoteForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface INoteService extends IService<NoteDO> {

  List<NoteDO> findAllByUsername(String username);

  NoteDO addNote(String username, NoteForm form);

  NoteDO updateNote(String username, NoteForm form);

  NoteDO updateNote(String username, NoteForm form, Boolean refreshUpdateTime);

  NoteDO deleteNote(String username, String noteId);
}
