$(document).ready(function(){
    $("#btn1").click( function createMessage() {
        // (1)
        var container = document.createElement('div')

        // (2)
        container.innerHTML = '<div class="my-message"> \
    <div class="my-message-title">'+'aeg'+'</div> \
    <div class="my-message-body">'+'eag'+'</div> \
    <input class="my-message-ok" type="button" value="OK"/> \
  </div>'

        // (3)
        return container.firstChild
    });
});