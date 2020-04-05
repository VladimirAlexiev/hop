/*! ******************************************************************************
 *
 * Hop : The Hop Orchestration Platform
 *
 * http://www.project-hop.org
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.apache.hop.job.entries.tableexists;

import org.apache.commons.lang.StringUtils;
import org.apache.hop.core.Const;
import org.apache.hop.core.annotations.PluginDialog;
import org.apache.hop.core.database.Database;
import org.apache.hop.core.database.DatabaseMeta;
import org.apache.hop.core.util.Utils;
import org.apache.hop.i18n.BaseMessages;
import org.apache.hop.job.JobMeta;
import org.apache.hop.job.entry.IJobEntryDialog;
import org.apache.hop.job.entry.IJobEntry;
import org.apache.hop.ui.core.database.dialog.DatabaseExplorerDialog;
import org.apache.hop.ui.core.dialog.EnterSelectionDialog;
import org.apache.hop.ui.core.dialog.ErrorDialog;
import org.apache.hop.ui.core.gui.WindowProperty;
import org.apache.hop.ui.core.widget.MetaSelectionLine;
import org.apache.hop.ui.core.widget.TextVar;
import org.apache.hop.ui.job.dialog.JobDialog;
import org.apache.hop.ui.job.entry.JobEntryDialog;
import org.apache.hop.ui.pipeline.transform.BaseTransformDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This dialog allows you to edit the Table Exists job entry settings. (select the connection and the table to be
 * checked) This entry type evaluates!
 *
 * @author Matt
 * @since 19-06-2003
 */
@PluginDialog( 
		  id = "TABLE_EXISTS", 
		  image = "TableExists.svg", 
		  pluginType = PluginDialog.PluginType.JOBENTRY,
		  documentationUrl = "https://www.project-hop.org/manual/latest/plugins/actions/"
)
public class JobEntryTableExistsDialog extends JobEntryDialog implements IJobEntryDialog {
  private static Class<?> PKG = JobEntryTableExists.class; // for i18n purposes, needed by Translator!!

  private Label wlName;

  private Text wName;

  private FormData fdlName, fdName;

  private MetaSelectionLine<DatabaseMeta> wConnection;

  private Label wlTablename;

  private TextVar wTablename;

  private Button wbTable;

  private FormData fdbSchema;
  private Button wbSchema;

  private FormData fdlTablename, fdTablename;

  private Label wlSchemaname;

  private TextVar wSchemaname;

  private FormData fdlSchemaname, fdSchemaname;

  private Button wOK, wCancel;

  private Listener lsOK, lsCancel;

  private JobEntryTableExists jobEntry;

  private Shell shell;

  private SelectionAdapter lsDef;

  private boolean changed;

  public JobEntryTableExistsDialog( Shell parent, IJobEntry jobEntryInt, JobMeta jobMeta ) {
    super( parent, jobEntryInt, jobMeta );
    jobEntry = (JobEntryTableExists) jobEntryInt;
    if ( this.jobEntry.getName() == null ) {
      this.jobEntry.setName( BaseMessages.getString( PKG, "JobTableExists.Name.Default" ) );
    }
  }

  public IJobEntry open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell( parent, props.getJobsDialogStyle() );
    props.setLook( shell );
    JobDialog.setShellImage( shell, jobEntry );

    ModifyListener lsMod = new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        jobEntry.setChanged();
      }
    };
    changed = jobEntry.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout( formLayout );
    shell.setText( BaseMessages.getString( PKG, "JobTableExists.Title" ) );

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Filename line
    wlName = new Label( shell, SWT.RIGHT );
    wlName.setText( BaseMessages.getString( PKG, "JobTableExists.Name.Label" ) );
    props.setLook( wlName );
    fdlName = new FormData();
    fdlName.left = new FormAttachment( 0, 0 );
    fdlName.right = new FormAttachment( middle, -margin );
    fdlName.top = new FormAttachment( 0, margin );
    wlName.setLayoutData( fdlName );
    wName = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wName );
    wName.addModifyListener( lsMod );
    fdName = new FormData();
    fdName.left = new FormAttachment( middle, 0 );
    fdName.top = new FormAttachment( 0, margin );
    fdName.right = new FormAttachment( 100, 0 );
    wName.setLayoutData( fdName );

    // Connection line
    wConnection = addConnectionLine( shell, wName, jobEntry.getDatabase(), lsMod );

    // Schema name line
    wlSchemaname = new Label( shell, SWT.RIGHT );
    wlSchemaname.setText( BaseMessages.getString( PKG, "JobTableExists.Schemaname.Label" ) );
    props.setLook( wlSchemaname );
    fdlSchemaname = new FormData();
    fdlSchemaname.left = new FormAttachment( 0, 0 );
    fdlSchemaname.right = new FormAttachment( middle, -margin );
    fdlSchemaname.top = new FormAttachment( wConnection, 2 * margin );
    wlSchemaname.setLayoutData( fdlSchemaname );

    wbSchema = new Button( shell, SWT.PUSH | SWT.CENTER );
    props.setLook( wbSchema );
    wbSchema.setText( BaseMessages.getString( PKG, "System.Button.Browse" ) );
    fdbSchema = new FormData();
    fdbSchema.top = new FormAttachment( wConnection, 2 * margin );
    fdbSchema.right = new FormAttachment( 100, 0 );
    wbSchema.setLayoutData( fdbSchema );
    wbSchema.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        getSchemaNames();
      }
    } );

    wSchemaname = new TextVar( jobMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wSchemaname );
    wSchemaname.addModifyListener( lsMod );
    fdSchemaname = new FormData();
    fdSchemaname.left = new FormAttachment( middle, 0 );
    fdSchemaname.top = new FormAttachment( wConnection, 2 * margin );
    fdSchemaname.right = new FormAttachment( wbSchema, -margin );
    wSchemaname.setLayoutData( fdSchemaname );

    // Table name line
    wlTablename = new Label( shell, SWT.RIGHT );
    wlTablename.setText( BaseMessages.getString( PKG, "JobTableExists.Tablename.Label" ) );
    props.setLook( wlTablename );
    fdlTablename = new FormData();
    fdlTablename.left = new FormAttachment( 0, 0 );
    fdlTablename.right = new FormAttachment( middle, -margin );
    fdlTablename.top = new FormAttachment( wbSchema, margin );
    wlTablename.setLayoutData( fdlTablename );

    wbTable = new Button( shell, SWT.PUSH | SWT.CENTER );
    props.setLook( wbTable );
    wbTable.setText( BaseMessages.getString( PKG, "System.Button.Browse" ) );
    FormData fdbTable = new FormData();
    fdbTable.right = new FormAttachment( 100, 0 );
    fdbTable.top = new FormAttachment( wbSchema, margin );
    wbTable.setLayoutData( fdbTable );
    wbTable.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        getTableName();
      }
    } );

    wTablename = new TextVar( jobMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wTablename );
    wTablename.addModifyListener( lsMod );
    fdTablename = new FormData();
    fdTablename.left = new FormAttachment( middle, 0 );
    fdTablename.top = new FormAttachment( wbSchema, margin );
    fdTablename.right = new FormAttachment( wbTable, -margin );
    wTablename.setLayoutData( fdTablename );

    wOK = new Button( shell, SWT.PUSH );
    wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
    FormData fd = new FormData();
    fd.right = new FormAttachment( 50, -10 );
    fd.bottom = new FormAttachment( 100, 0 );
    fd.width = 100;
    wOK.setLayoutData( fd );

    wCancel = new Button( shell, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );
    fd = new FormData();
    fd.left = new FormAttachment( 50, 10 );
    fd.bottom = new FormAttachment( 100, 0 );
    fd.width = 100;
    wCancel.setLayoutData( fd );

    // Add listeners
    lsCancel = new Listener() {
      public void handleEvent( Event e ) {
        cancel();
      }
    };
    lsOK = new Listener() {
      public void handleEvent( Event e ) {
        ok();
      }
    };

    wCancel.addListener( SWT.Selection, lsCancel );
    wOK.addListener( SWT.Selection, lsOK );
    BaseTransformDialog.positionBottomButtons( shell, new Button[] { wOK, wCancel }, margin, wTablename );
    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected( SelectionEvent e ) {
        ok();
      }
    };

    wName.addSelectionListener( lsDef );
    wTablename.addSelectionListener( lsDef );

    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );

    getData();

    BaseTransformDialog.setSize( shell );

    shell.open();
    props.setDialogSize( shell, "JobTableExistsDialogSize" );
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return jobEntry;
  }

  public void dispose() {
    WindowProperty winprop = new WindowProperty( shell );
    props.setScreen( winprop );
    shell.dispose();
  }

  /**
   * Copy information from the meta-data input to the dialog fields.
   */
  public void getData() {
    wName.setText( Const.nullToEmpty( jobEntry.getName() ) );
    wTablename.setText( Const.nullToEmpty( jobEntry.getTablename() ) );
    wSchemaname.setText( Const.nullToEmpty( jobEntry.getSchemaname() ) );
    if ( jobEntry.getDatabase() != null ) {
      wConnection.setText( jobEntry.getDatabase().getName() );
    }

    wName.selectAll();
    wName.setFocus();
  }

  private void cancel() {
    jobEntry.setChanged( changed );
    jobEntry = null;
    dispose();
  }

  private void ok() {
    if ( Utils.isEmpty( wName.getText() ) ) {
      MessageBox mb = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
      mb.setText( BaseMessages.getString( PKG, "System.TransformJobEntryNameMissing.Title" ) );
      mb.setMessage( BaseMessages.getString( PKG, "System.JobEntryNameMissing.Msg" ) );
      mb.open();
      return;
    }
    jobEntry.setName( wName.getText() );
    jobEntry.setDatabase( jobMeta.findDatabase( wConnection.getText() ) );
    jobEntry.setTablename( wTablename.getText() );
    jobEntry.setSchemaname( wSchemaname.getText() );

    dispose();
  }

  private void getSchemaNames() {
    if ( wSchemaname.isDisposed() ) {
      return;
    }
    DatabaseMeta databaseMeta = jobMeta.findDatabase( wConnection.getText() );
    if ( databaseMeta != null ) {
      Database database = new Database( loggingObject, databaseMeta );
      database.shareVariablesWith( jobMeta );
      try {
        database.connect();
        String[] schemas = database.getSchemas();

        if ( null != schemas && schemas.length > 0 ) {
          schemas = Const.sortStrings( schemas );
          EnterSelectionDialog dialog = new EnterSelectionDialog( shell, schemas,
            BaseMessages.getString( PKG, "System.Dialog.AvailableSchemas.Title", wConnection.getText() ),
            BaseMessages.getString( PKG, "System.Dialog.AvailableSchemas.Message" ) );
          String d = dialog.open();
          if ( d != null ) {
            wSchemaname.setText( Const.NVL( d.toString(), "" ) );
          }

        } else {
          MessageBox mb = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
          mb.setMessage( BaseMessages.getString( PKG, "System.Dialog.AvailableSchemas.Empty.Message" ) );
          mb.setText( BaseMessages.getString( PKG, "System.Dialog.AvailableSchemas.Empty.Title" ) );
          mb.open();
        }
      } catch ( Exception e ) {
        new ErrorDialog( shell, BaseMessages.getString( PKG, "System.Dialog.Error.Title" ),
          BaseMessages.getString( PKG, "System.Dialog.AvailableSchemas.ConnectionError" ), e );
      } finally {
        if ( database != null ) {
          database.disconnect();
          database = null;
        }
      }
    }
  }

  private void getTableName() {
    String databaseName = wConnection.getText();
    if ( StringUtils.isNotEmpty( databaseName ) ) {
      DatabaseMeta databaseMeta = jobMeta.findDatabase( databaseName );
      if ( databaseMeta != null ) {
        DatabaseExplorerDialog std = new DatabaseExplorerDialog( shell, SWT.NONE, databaseMeta, jobMeta.getDatabases() );
        std.setSelectedSchemaAndTable( wSchemaname.getText(), wTablename.getText() );
        if ( std.open() ) {
          wSchemaname.setText( Const.NVL( std.getSchemaName(), "" ) );
          wTablename.setText( Const.NVL( std.getTableName(), "" ) );
        }
      } else {
        MessageBox mb = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
        mb.setMessage( BaseMessages.getString( PKG, "System.Dialog.ConnectionError.DialogMessage" ) );
        mb.setText( BaseMessages.getString( PKG, "System.Dialog.Error.Title" ) );
        mb.open();
      }
    }
  }
}