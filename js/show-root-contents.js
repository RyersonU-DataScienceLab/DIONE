
  var request = new XMLHttpRequest;

  //get the folders under root
  var request;
  request.open("GET","apis/show-root-folders.php");
  request.send(null);
  request.onreadystatechange = function(){
    if(request.readyState == XMLHttpRequest.DONE){
      var folders = request.responseText;
      folders = JSON.parse(folders);
      console.log(folders);
      for (var i = 0; i < folders.length; i++) {
        var folder_text = folders[i].folder_name;
        var folder_id   = folders[i].id;

        //creating a checkbox for action
        var folder_action  = document.createElement("input");
        folder_action.setAttribute("id",folder_id);
        folder_action.setAttribute("type","checkbox");
        folder_action.setAttribute("name","folder");
        folder_action.setAttribute("onclick","show_folder_contents(this)");

        //creating a folder
        var folder = document.createElement("label");
        folder.setAttribute("for",folder_id);
        folder.setAttribute("class","folder");
        var folder_name = document.createTextNode(folder_text);
        folder.appendChild(folder_name);

        //creating folder container - for children
        var folder_container = document.createElement("div");
        folder_container.setAttribute("class","folder-container");

        //creating container for above elements
        var f_container = document.createElement("div");
        f_container.setAttribute("id","f_"+folder_id);
        f_container.setAttribute("class","f_container");
        f_container.appendChild(folder_action);
        f_container.appendChild(folder);
        f_container.appendChild(folder_container);


        //appending to the parent container
        var container = document.getElementById("f_1");
        container = container.getElementsByClassName("folder-container")[0];
        container.appendChild(f_container);
      }
      var element = getSelected("folder");
      decorateSelected(element,"folder");
      request_projects();
    }
  }

  //get the projects under root
  function request_projects(){
    var projects_request = new XMLHttpRequest;
    projects_request.open("GET","apis/show-root-projects.php");
    projects_request.send(null);
    projects_request.onreadystatechange = function(){
      if(projects_request.readyState == XMLHttpRequest.DONE){
        var projects = projects_request.responseText;
        projects = JSON.parse(projects);
        if(projects.length > 0){
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
            var container = document.getElementById("f_1");
            container = container.getElementsByClassName("folder-container")[0];
            container.appendChild(project);
          }
          get_project(projects[0].id);
        }
      }
    }
  }
