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

package org.apache.hop.ui.hopgui.delegates;

import org.apache.hop.ui.hopgui.HopGui;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;

import java.util.concurrent.atomic.AtomicBoolean;

public class HopGuiDirectoryDialogExtension {
  public AtomicBoolean doIt;
  public DirectoryDialog directoryDialog;

  public HopGuiDirectoryDialogExtension( AtomicBoolean doIt, DirectoryDialog directoryDialog) {
    this.doIt = doIt;
    this.directoryDialog = directoryDialog;
  }

  /**
   * Gets doIt
   *
   * @return value of doIt
   */
  public AtomicBoolean getDoIt() {
    return doIt;
  }

  /**
   * @param doIt The doIt to set
   */
  public void setDoIt( AtomicBoolean doIt ) {
    this.doIt = doIt;
  }

  /**
   * Gets directoryDialog
   *
   * @return value of directoryDialog
   */
  public DirectoryDialog getDirectoryDialog() {
    return directoryDialog;
  }

  /**
   * @param directoryDialog The directoryDialog to set
   */
  public void setDirectoryDialog( DirectoryDialog directoryDialog ) {
    this.directoryDialog = directoryDialog;
  }
}