function addAdminLink(role) {
    if (role=="ADMIN"){
        var menu = $('#topMenu');
        var link = document.createElement("li");
        var ref = document.createElement("a");
        ref.href="/admin/panel";
        ref.text="Admin's panel";
        link.appendChild(ref);
        menu.append(link);
    }
}