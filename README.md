This small mod allows players with Ely.by accounts to enter the server with `online_mode=true` without interfering with players with Mojang accounts.

The mod just combines two authentication systems without interfering with the server itself.

First comes authentication with **Mojang** servers, and after an unsuccessful login, there is a check on **Ely.by**. If there and there did not pass the test, then the player is **not allowed** on the server.

In order to achieve this result, it was necessary to download the patch from the official site Ely.by, which was not convenient and prevented players with official accounts from logging in.

When using this mod, I recommend setting `enforce-secure-profile=false` in the server configuration to disable message signing that **Ely,by** players do not have.

At the moment, I could not make a plugin with such an idea and I'm unlikely to be able to, because this is my *first* project.

Config:
``` json
"AuthList":[ "order of execution" ],
"AuthSchema":{
    "name":{
        "url_check": "url request hasJoinedServer"
        "url_property" : "url reqest profile property (skin&cape)"
        "AddProperty" : [{"custom" : "property"},
            {"name":"ely","value":"why not"}]
    }
},
"dubug" : "all / detal / (not necessary)"     
```   
