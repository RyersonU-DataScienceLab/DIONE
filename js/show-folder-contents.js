function show_folder_contents(parent_folder,dropdown = false){

  if(dropdown){parent_folder_id = parent_folder.id.split("_")[1];}
  else{parent_folder_id = parent_folder.id;}
  if(parent_folder.checked){

    // get folders under the parent folder
    var folders_request = new XMLHttpRequest;
    var data = new FormData();
    data.set("parent_folder_id",parent_folder_id);
    folders_request.open("POST","apis/show-folder-contents.php");
    folders_request.send(data);
    folders_request.onreadystatechange = function(){
      if(folders_request.readyState == XMLHttpRequest.DONE){
        var folders = folders_request.responseText;
        folders = JSON.parse(folders);
        for (var i = 0; i < folders.length; i++) {
          var folder_text = folders[i].folder_name;
          var folder_id   = folders[i].id;

          //creating a checkbox for action
          var folder_action  = document.createElement("input");
          if(dropdown){folder_action.setAttribute("id","dropdown_"+folder_id);}
          else{folder_action.setAttribute("id",folder_id);}
          folder_action.setAttribute("type","checkbox");
          folder_action.setAttribute("name","folder");
          folder_action.setAttribute("value",folder_text);
          if(dropdown){folder_action.setAttribute("onclick","show_folder_contents(this,true)");}
          else{folder_action.setAttribute("onclick","show_folder_contents(this)");}

          //creating a folder
          var folder = document.createElement("label");
          if(dropdown){folder.setAttribute("for","dropdown_"+folder_id);}
          else{folder.setAttribute("for",folder_id);}
          folder.setAttribute("class","folder");
          var folder_name = document.createTextNode(folder_text);
          folder.appendChild(folder_name);

          //creating folder container - for children
          var folder_container = document.createElement("div");
          folder_container.setAttribute("class","folder-container");

          //creating container for above elements
          var f_container = document.createElement("div");
          if(dropdown){f_container.setAttribute("id","f_dropdown_"+folder_id);}
          else{f_container.setAttribute("id","f_"+folder_id);}
          f_container.setAttribute("class","f_container");
          f_container.appendChild(folder_action);
          f_container.appendChild(folder);
          f_container.appendChild(folder_container);


          //appending to the parent container
          console.log(parent_folder_id);
          if(dropdown){var container = document.getElementById("f_dropdown_"+parent_folder_id);}
          else{var container = document.getElementById("f_"+parent_folder_id);}
          container = container.getElementsByClassName("folder-container")[0];
          container.appendChild(f_container);
        }
        if(dropdown){
          var value_holder = document.querySelector("#folder_value_for_new_project");
          value_holder.textContent = parent_folder.value;
          value_holder.setAttribute("data-value",parent_folder_id);
        }
      }
    }
    //get projects under the parent folder
    if(!dropdown){
      var request_projects = new XMLHttpRequest;
      var data = new FormData();
      data.set("parent_folder_id",parent_folder_id);
      request_projects.open("POST","apis/get-projects.php");
      request_projects.send(data);
      request_projects.onreadystatechange = function(){
        if(request_projects.readyState == XMLHttpRequest.DONE){
          var projects = request_projects.responseText;
          projects = JSON.parse(projects);
          for (var i = 0; i < projects.length; i++) {
            var project_text = projects[i].project_name;
            var project_id   = projects[i].id;

            //creating a project
            var project = document.createElement("div");
            project.setAttribute("class","project");
            project.setAttribute("id",project_id);
            project.setAttribute("onclick","get_project('"+project_id+"')");

            var project_name = document.createTextNode(project_text);
            project.appendChild(project_name);


            // appending to the container
            var container = document.getElementById("f_"+parent_folder_id);
            container = container.getElementsByClassName("folder-container")[0];
            container.appendChild(project);
          }

        }
      }
    }
  }
  else{
    if(dropdown){var container = document.getElementById("f_dropdown_"+parent_folder_id);}
    else{var container = document.getElementById("f_"+parent_folder_id);}
    container = container.getElementsByClassName("folder-container")[0];
    while(container.hasChildNodes()){
      container.removeChild(container.firstChild);
    }
  }
}
