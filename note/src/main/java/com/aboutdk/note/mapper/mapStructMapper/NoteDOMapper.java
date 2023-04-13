package com.aboutdk.note.mapper.mapStructMapper;

import com.aboutdk.note.POJO.DO.NoteDO;
import com.aboutdk.note.POJO.FORM.NoteForm;
import com.aboutdk.note.POJO.VO.NoteVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoteDOMapper {

   NoteDOMapper INSTANCE = Mappers.getMapper( NoteDOMapper.class );

   NoteVO noteDOToNoteVO(NoteDO noteDO);

   NoteForm noteDOToNoteForm(NoteDO noteDO);
}
