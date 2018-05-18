$(document).ready(function() {
    //Hide all alerts
    $("#wishlist-success").hide();
    $("#search-warning").hide();
    $("#search-char-warning").hide();
    $("#search-fail").hide();

    //Check if enter is being pressed while the search-field is focused
    $('#search-field').keypress(function(e) {
        if (e.keyCode == 13) {
            //Run the get_games()-function by clicking the search-button
            $('#get-games').click();
            //Unfocus (blur) the search-field
            $('#search-field').blur();
        }
    });
});


function get_games() {
    //Get search term
    var search = $('#search-field').val();
    //Check if deal_list exists

    if (localStorage.getItem("deal_list") == undefined) {
        //If it does not exist, create a new, empty deal_list
        var deal_list = [];
    } else {
        //If it does exist, get it from localStorage
        var deal_list = JSON.parse(localStorage.getItem("deal_list"));
    }

    //Checks for illegal characters

    if (/^[a-zA-z0-9- ]*$/.test(search) == false) {
        $("#search-char-warning").alert();
        $("#search-char-warning").fadeTo(2000, 500).slideUp(500, function() {
            $("#search-char-warning").hide();
        });
    } else {
        //Replaces space with %20 (API parameter rules)
        var fixedSearch = search.split(' ').join('%20');
        //Make ajax call with fixed search term
        $.ajax({
            url: "http://www.cheapshark.com/api/1.0/games?title=" + fixedSearch + "&r=json",
            dataType: "JSON"
        }).done(function(data) {
            //Clear search field
            $("#search-results").empty();
            var jsonData = data;
            //Checks if there are any search results
            if (jsonData.length != 0) {
                //Loop through response
                for (i = 0; i < jsonData.length; i++) {
                    //Create markup with correct title and year
                    var html_block = '<figure class = "game-figure">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].cheapestDealID + '">\
                                  <img class="game-image" src=' + jsonData[i].thumb + ' alt="' + jsonData[i].external + '">\
                                </a>\
                              <div class="caption">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].cheapestDealID + '">\
                                  <h4 class="game-title">' + jsonData[i].external + '</h4>\
                                </a>\
                              </div>\
                              <div class="row">\
                                <div class="game-div">\
                                  <p class="game-price game-text">$' + jsonData[i].cheapest + '</p>\
                                </div>\
                                <div class="game-div">\
                                  <button id="addToWishlist' + i + '" class="btn btn-info">Lägg till i önskelista</button>\
                                </div>\
                              </div>\
                            </figure>';
                    //Append block of HTML to list of search results
                    $("#search-results").append(html_block);
                    $("#search-result").show();

                    //If there is an error getting a thumbnail, use a 404-thumbnail instead
                    $("img").error(function() {
                        $(this).attr("src", "img/404-thumb.png");
                    });
                }

                $("#search-results button").click(function() {
                    //Disable the clicked button
                    $(this).prop('disabled', true);
                    //Get the index of the buttons great grandparent(list item)
                    var index = $(this).parent().parent().parent().index();
                    //Stringify dealID
                    var archived_deal = JSON.stringify(jsonData[index].cheapestDealID);
                    var dealTitle = jsonData[index].external;
                    //Alert the user which game deal was added to the wishlist
                    $("#wishlist-success").alert();
                    $("#wishlist-success").fadeTo(2000, 500).slideUp(500, function() {
                        $("#wishlist-success").hide();
                    });
                    //Append deal to array
                    deal_list.push(archived_deal);
                    //Stringify array and save to localStorage
                    localStorage.setItem("deal_list", JSON.stringify(deal_list));
                })
            } else {
                //If there are no search results, tell the user
                $("#search-warning").alert();
                $("#search-warning").fadeTo(2000, 500).slideUp(500, function() {
                    $("#search-warning").hide();
                });
            }


        }).fail(function(data) {
            //Tell the user the Ajax call failed
            alert('Sökning misslyckades, försök igen senare!')
            $("#search-fail").alert();
            $("#search-fail").fadeTo(2000, 500).slideUp(500, function() {
                $("#search-fail").hide();
            });
        });
    }
};