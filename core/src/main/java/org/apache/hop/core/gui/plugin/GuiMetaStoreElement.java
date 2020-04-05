package org.apache.hop.core.gui.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.TYPE, } )
public @interface GuiMetaStoreElement {
  String name();
  String description();
  String iconImage();
}