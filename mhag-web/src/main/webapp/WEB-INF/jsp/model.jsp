<!DOCTYPE HTML>
<html>
    <head>
        <%@include file="includes.jsp" %>
        <title>Mhag</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="description" content="Monster Hunter armour generator to help hunters find their perfect armors" />

        <link rel="shortcut icon" href="Favicon.ico"/>

        <link type="text/css" rel="stylesheet" media="all" href="<c:url value="/styles/style.css"/>"/>
        <link type="text/css" rel="stylesheet" media="all" href="<c:url value="/styles/jquery-ui.custom.css"/>"/>

        <script type="text/javascript" src="<c:url value="/scripts/jquery-1.10.2.js"/>" charset="UTF-8"></script>
        <script type="text/javascript" src="<c:url value="/scripts/jquery-ui-1.10.4.custom.min.js"/>" charset="UTF-8"></script>
        <script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>

        <script type="text/javascript" charset="UTF-8">
            $(function() {
			    $( "#tabs" ).tabs(
//                    {
//                        select: function(event, ui) {
//                           // todo test removing this so we can cache some pages, adding a time stamp to those we do not want to cache
//                            if ($(ui.index != 0)) {
//                                if($.data(ui.tab, 'load.tabs')) {
//                                    $(ui.panel).html("Loading...");
//                                }
//                            }
//                        }
//                    }
                );
		    });
        </script>
    </head>

    <body>

        <c:import url="${content}"/>

    </body>
</html>