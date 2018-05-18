$(document).ready(function() {
    $("#wishlist-success").hide();
    //Empty the wishlist
    $("#wishlist").empty();
    //Get wishlist from localStorage
    var wishlist = JSON.parse(localStorage.getItem("deal_list"));
    //Iterate through all items in the wishlist
    for (i = 0; i < wishlist.length; i++) {
        var dealID = wishlist[i];
        //Make Ajax-call with correct dealID
        $.ajax({
            url: "http://www.cheapshark.com/api/1.0/deals?id=" + dealID,
            dataType: "JSON"
        }).done(function(data) {
            //Check if there is a search result
            if (data.length != 0) {
                //Create markup with correct information
                var html_block = '<li class="game-figure">\
                              <a href="http://www.cheapshark.com/redirect?dealID=' + dealID + '">\
                                <img class="game-image" src=' + data.gameInfo.thumb + ' alt="' + data.gameInfo.name + '">\
                              </a>\
                            <div class="caption">\
                              <a href="http://www.cheapshark.com/redirect?dealID=' + dealID + '">\
                                <h4 class="group inner list-group-item-heading">' + data.gameInfo.name + '</h4>\
                              </a>\
                            </div>\
                            <div class="row">\
                              <div class="col-xs-12">\
                                <p class="group inner list-group-item-text">$' + data.gameInfo.salePrice + '</p>\
                              </div>\
                              <div class="col-xs-12">\
                                <button id="deal' + i + '" type="button" class="btn btn-danger btn-sm delete">\
                                  <span class="glyphicon glyphicon-trash"></span>\
                                </button>\
                              </div>\
                            </div>\
                          </li>';
                var c = $(html_block);
                //Append markup to ul
                $("#wishlist").append(c);

                //If a delete-button gets clicked
                c.find("button").on("click", function() {
                    //Get index of the buttons great grandparent (li)
                    var index = $(this).parent().parent().parent().index();
                    //Remove correct item from list
                    wishlist.splice(index, 1);
                    //Also remove it from localStorage
                    localStorage.removeItem(wishlist[index]);
                    //Update the list in localStorage
                    localStorage.setItem("deal_list", JSON.stringify(wishlist));
                    //Alert user a deal has been deleted
                    $("#wishlist-success").alert();
                    $("#wishlist-success").fadeTo(2000, 500).slideUp(500, function() {
                        $("#wishlist-success").hide();
                    });
                    //Reload the page after a small delay
                    setTimeout(function() { location.reload() }, 1500);
                });
            }
        });
    }
});