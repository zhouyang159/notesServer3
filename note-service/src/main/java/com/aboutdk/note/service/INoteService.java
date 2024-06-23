package com.aboutdk.note.service;

import com.aboutdk.note.POJO.DO.NoteDO;
import com.aboutdk.note.POJO.FORM.NoteForm;

import java.util.List;

public interface INoteService {

  List<NoteDO> findAllByUsername(String username);

  NoteDO findById(String id);

  NoteDO addNote(String username, NoteForm form);

  NoteDO updateNote(String username, NoteForm form);

  NoteDO updateNote(String username, NoteForm form, Boolean refreshUpdateTime);

  NoteDO deleteNote(String username, String noteId);
}
