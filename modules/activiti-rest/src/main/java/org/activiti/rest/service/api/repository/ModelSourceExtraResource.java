/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.rest.service.api.repository;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.repository.Model;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author Frederik Heremans
 */
@RestController
public class ModelSourceExtraResource extends BaseModelSourceResource {

  @RequestMapping(value="/repository/models/{modelId}/source-extra", method = RequestMethod.GET)
  protected @ResponseBody byte[] getModelBytes(@PathVariable String modelId, HttpServletResponse response) {
    byte[] editorSource = repositoryService.getModelEditorSourceExtra(modelId);
    if (editorSource == null) {
      throw new ActivitiObjectNotFoundException("Model with id '" + modelId + "' does not have extra source available.", String.class);
    }
    response.setContentType("application/octet-stream");
    return editorSource;
  }

  @RequestMapping(value="/repository/models/{modelId}/source-extra", method = RequestMethod.PUT)
  protected void setModelSource(@PathVariable String modelId, @RequestParam("file") MultipartFile file, HttpServletResponse response) {
    Model model = getModelFromRequest(modelId);
    if (model != null) {
      try {
        repositoryService.addModelEditorSourceExtra(model.getId(), file.getBytes());
        response.setStatus(HttpStatus.SC_NO_CONTENT);
      } catch (Exception e) {
        throw new ActivitiException("Error adding model editor source extra", e);
      }
    }
  }
  
}
