package com.aboutdk.note.POJO.FORM;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class NoteForm {

  @NotEmpty
  private String id;

  @NotEmpty
  private String title;

  @NotEmpty
  private String content;

  private Integer number = 0;

  private Boolean encrypt = false;

  @Min(value = 0)
  @Max(value = 1)
  private Integer deleted = 0;

  private Integer version = 0;
}
