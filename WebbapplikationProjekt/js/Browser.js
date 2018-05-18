$(document).ready(function() {
    //Hide alerts
    $("#wishlist-success").hide();
});

function get_games_sale() {
    //Check if deal_list exists
    if (localStorage.getItem("deal_list") == undefined) {
        //If it does not exist, create a new, empty deal_list
        var deal_list = [];
    } else {
        //If it does exist, get it from localStorage
        var deal_list = JSON.parse(localStorage.getItem("deal_list"));
    }
    //Make ajax call for correct category
    $.ajax({
        url: "http://www.cheapshark.com/api/1.0/deals?sortBy=Metacritic&r=json",
        dataType: "JSON"
    }).done(function(data) {
        //Clear search field
        $("#search-results").empty();
        var jsonData = data;
        //Checks if there are any search results
        if (jsonData != undefined && jsonData.length != 0) {
            //Loop through response
            for (i = 0; i < jsonData.length; i++) {

                //Create markup with correct information
                var html_block = '<li class="game-figure">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].dealID + '">\
                                  <img class="game-image" src=' + jsonData[i].thumb + '  alt="' + jsonData[i].external + '">\
                                </a>\
                              <div class="caption">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].dealID + '">\
                                  <h4 class="game-title">' + jsonData[i].title + '</h4>\
                                </a>\
                              </div>\
                              <div class="row">\
                                <div class="game-div">\
                                  <p class="game-rating game-text">Omdömespoäng: ' + jsonData[i].metacriticScore + '</p>\
                                </div>\
                                <div class="game-div"">\
                                  <p class="game-price game-text">$' + jsonData[i].salePrice + '</p>\
                                </div>\
                                <div class="game-div"">\
                                  <button id="addToWishlist' + i + '" class="btn btn-info">Lägg till i önskelista</button>\
                                </div>\
                              </div>\
                            </li>';

                //Append block of HTML to list of search results
                $("#search-results").append(html_block);


            }
            $("#search-results button").click(function() {
                $(this).prop('disabled', true);
                //Get the index of the buttons great grandparent(list item)
                var index = $(this).parent().parent().parent().index();
                //Stringify dealID
                var archived_deal = JSON.stringify(jsonData[index].dealID);
                var dealTitle = jsonData[index].title;
                //Alert the user a game deal was added to the wishlist
                $("#wishlist-success").alert();
                $("#wishlist-success").fadeTo(2000, 500).slideUp(500, function() {
                    $("#wishlist-success").hide();
                });
                //Append deal to array
                deal_list.push(archived_deal);
                //Stringify array and save to localStorage
                localStorage.setItem("deal_list", JSON.stringify(deal_list));
            })
        }
    });
};

function get_games_rating() {
    //Check if deal_list exists
    if (localStorage.getItem("deal_list") == undefined) {
        //If it does not exist, create a new, empty deal_list
        var deal_list = [];
    } else {
        //If it does exist, get it from localStorage
        var deal_list = JSON.parse(localStorage.getItem("deal_list"));
    }
    $.ajax({
        url: "http://www.cheapshark.com/api/1.0/deals?onSale=1&r=json",
        dataType: "JSON"
    }).done(function(data) {
        //Clear search field
        $("#search-results").empty();
        var jsonData = data;
        //Checks if there are any search results
        if (jsonData != undefined && jsonData.length != 0) {
            //Loop through response
            for (i = 0; i < jsonData.length; i++) {

                //Create markup with correct information
                var html_block = '<li class="game-figure">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].dealID + '">\
                                  <img class="game-image" src=' + jsonData[i].thumb + ' alt="' + jsonData[i].external + '">\
                                </a>\
                              <div class="caption">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].dealID + '">\
                                  <h4 class="game-title">' + jsonData[i].title + '</h4>\
                                </a>\
                              </div>\
                              <div class="row">\
                              <div class="game-div"">\
                                <p class="game-deal game-text">Erbjudandebetyg: ' + jsonData[i].dealRating + '/10</p>\
                              </div>\
                                <div class="game-div"">\
                                  <p class="game-price game-text">$' + jsonData[i].salePrice + '</p>\
                                </div>\
                                <div class="game-div"">\
                                  <button id="addToWishlist' + i + '" class="btn btn-info">Lägg till i önskelista</button>\
                                </div>\
                              </div>\
                            </li>';
                //Append block of HTML to list of search results
                $("#search-results").append(html_block);
            }

            $("#search-results button").click(function() {
                $(this).prop('disabled', true);
                //Get the index of the buttons great grandparent(list item)
                var index = $(this).parent().parent().parent().index();
                //Stringify dealID
                var archived_deal = JSON.stringify(jsonData[index].dealID);
                var dealTitle = jsonData[index].title;
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
        }
    });
};

function get_gamesavings() {
    //Check if deal_list exists
    if (localStorage.getItem("deal_list") == undefined) {
        //If it does not exist, create a new, empty deal_list
        var deal_list = [];
    } else {
        //If it does exist, get it from localStorage
        var deal_list = JSON.parse(localStorage.getItem("deal_list"));
    }
    $.ajax({
        url: "http://www.cheapshark.com/api/1.0/deals?sortBy=Savings&r=json",
        dataType: "JSON"
    }).done(function(data) {
        //Clear search field
        $("#search-results").empty();
        var jsonData = data;
        //Checks if there are any search results
        if (jsonData != undefined && jsonData.length != 0) {
            //Loop through response
            for (i = 0; i < jsonData.length; i++) {

                //Create markup with correct information
                var html_block = '<li class="game-figure">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].dealID + '">\
                                  <img class="game-image" src=' + jsonData[i].thumb + ' alt="' + jsonData[i].external + '">\
                                </a>\
                              <div class="caption">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].dealID + '">\
                                  <h4 class="game-title">' + jsonData[i].title + '</h4>\
                                </a>\
                              </div>\
                              <div class="row">\
                                <div class="game-div"">\
                                  <p class="game-price game-text">$' + jsonData[i].salePrice + '</p>\
                                </div>\
                                <div class="game-div"">\
                                  <p class="game-savings game-text">Besparing: $' + parseInt(jsonData[i].savings).toFixed(2) + '</p>\
                                </div>\
                                <div class="game-div"">\
                                  <button id="addToWishlist' + i + '" class="btn btn-info">Lägg till i önskelista</button>\
                                </div>\
                              </div>\
                            </li>';

                //Append block of HTML to list of search results
                $("#search-results").append(html_block);
            }

            //If a add-to-wishlist button gets clicked
            $("#search-results button").click(function() {
                $(this).prop('disabled', true);
                //Get the index of the buttons great grandparent(list item)
                var index = $(this).parent().parent().parent().index();
                //Stringify dealID
                var archived_deal = JSON.stringify(jsonData[index].dealID);
                var dealTitle = jsonData[index].title;
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
        }
    });
};

function get_gameReleaseDate() {
    //Check if deal_list exists
    if (localStorage.getItem("deal_list") == undefined) {
        //If it does not exist, create a new, empty deal_list
        var deal_list = [];
    } else {
        //If it does exist, get it from localStorage
        var deal_list = JSON.parse(localStorage.getItem("deal_list"));
    }
    $.ajax({
        url: "http://www.cheapshark.com/api/1.0/deals?sortBy=Release&r=json",
        dataType: "JSON"
    }).done(function(data) {
        //Clear search field
        $("#search-results").empty();
        var jsonData = data;
        //Checks if there are any search results
        if (jsonData != undefined && jsonData.length != 0) {
            //Loop through response
            for (i = 0; i < jsonData.length; i++) {
                //Convert from Unix timestamps to normal dates
                var a = new Date(jsonData[i].releaseDate * 1000);
                var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                var year = a.getFullYear();
                var month = months[a.getMonth()];
                var date = a.getDate();
                var time = date + ' ' + month + ' ' + year;

                //Create markup with correct information
                var html_block = '<li class="game-figure">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].dealID + '">\
                                  <img class="game-image" src=' + jsonData[i].thumb + ' alt="' + jsonData[i].external + '">\
                                </a>\
                              <div class="caption">\
                                <a href="http://www.cheapshark.com/redirect?dealID=' + jsonData[i].dealID + '">\
                                  <h4 class="game-title">' + jsonData[i].title + '</h4>\
                                </a>\
                              </div>\
                              <div class="row">\
                              <div class="game-div"">\
                                <p class="game-release game-text">' + time + '</p>\
                              </div>\
                                <div class="game-div"">\
                                  <p class="game-sale game-text">$' + jsonData[i].salePrice + '</p>\
                                </div>\
                                <div class="game-div"">\
                                  <button id="addToWishlist' + i + '" class="btn btn-info">Lägg till i önskelista</button>\
                                </div>\
                              </div>\
                            </li>';
                //Append block of HTML to list of search results
                $("#search-results").append(html_block);
            }

            $("#search-results button").click(function() {
                $(this).prop('disabled', true);
                //Get the index of the buttons great grandparent(list item)
                var index = $(this).parent().parent().parent().index();
                //Stringify dealID
                var archived_deal = JSON.stringify(jsonData[index].dealID);
                var dealTitle = jsonData[index].title;
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
        }
    });
};