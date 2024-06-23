package com.aboutdk.note.config;

import com.aboutdk.note.POJO.DO.NoteDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.*;

import static com.aboutdk.note.consts.NoteConst.DELETED_NOTE;

@Configuration
@Slf4j
public class DeleteNoteConfig {

  public DeleteNoteConfig() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 23);

    Date firstTime = calendar.getTime();

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        log.info("=================== empty trash regularly ===================");
        //balabala
      }
    }, firstTime, 24 * 60 * 60 * 1000);
  }
}
