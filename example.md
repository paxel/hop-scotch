# JSON
```json
{
  "glossary": {
    "title": "example glossary",
    "GlossDiv": {
      "title": "S",
      "GlossList": [
        {
          "GlossEntry": {
            "ID": "SGML",
            "SortAs": "SGML",
            "GlossTerm": "Standard Generalized Markup Language",
            "Acronym": "SGML",
            "Abbrev": "ISO 8879:1986",
            "GlossDef": {
              "para": "A meta-markup language, used to create markup languages such as DocBook.",
              "GlossSeeAlso": [
                "GML",
                "XML"
              ]
            },
            "GlossSee": "markup"
          }
        },
        {
          "GlossEntry": {
            "ID": "XML",
            "SortAs": "XML",
            "GlossTerm": "Extensible Markup Language",
            "Acronym": "XML",
            "GlossDef": {
              "GlossSeeAlso": [
                "GML",
                "SGML"
              ]
            },
            "GlossSee": "markup"
          }
        }
      ]
    }
  }
}
```


# cjson
## generated structs from the file
```
!default type:string #all omitted types are string
!struct GlossDef {para, GlossSeeAlso:string[]}
!struct GlossEntry {ID, SortAs, GlossTerm, Acronym, Abbrev, GlossDef:GlossDef, GlossSee}
!struct GlossList {GlossEntry:GlossEntry}
!struct GlossDiv {title, GlossList:GlossList[]}
!struct glossary {title, GlossDiv:GlossDiv}
!struct doc {glossary:glossary} # defines the format of the data
```

## option 1 - code mode: all is put in variables

```
s_gd1 = GlossDef ( "A meta-markup language, used to create markup languages such as DocBook.", ["GML", "XML"] )
s_ge1 = GlossEntry ( "SGML", "SGML", "Standard Generalized Markup Language", "SGML", "ISO 8879:1986", s_gd1, "markup" )
s_gd2 = GlossDef ( !, ["GML", "SGML"] )
s_ge2 = GlossEntry ( "XML", "XML", "Extensible Markup Language", "XML", !, s_gd2, "markup" )
s_gl1 = GlossList ( s_ge1 )
s_gl2 = GlossList ( s_ge2 )
s_gdiv = GlossDiv ( "S", [s_gl1,s_gl2] )
s_glossary = glossary ( "example glossary", s_gdiv )
doc( s_glossary ) # doc(...) defines the value of the file
```

## option 2 - compact mode: only large data is put in variables

```
definition1 = GlossDef( "A meta-markup language, used to create markup languages such as DocBook.", ["GML", "XML"] )
entry1 = GlossEntry( "SGML", "SGML", GlossTerm="Standard Generalized Markup Language", "SGML", Abrev="ISO 8879:1986", definition1, "markup" )
definition2 = GlossDef( !, ["GML", "SGML"] )
entry2 = GlossEntry( "XML", "XML", GlossTerm="Extensible Markup Language", "SGML", Abrev=!, definition2, "markup" )
doc( glossary ( "example glossary", GlossDiv ( "S",[ GlossList ( entry1 ), GlossList ( entry2 )] ) ) )
```

