def property(row: list, listed_dataframe: list, column_heads: list):
    for row in listed_dataframe:
        if row !=listed_dataframe[0]:
            i = 0
            for prop in row :
                print("""
                    <owl:Class rdf:ID = "{0}":"{1}"
            """ .format(column_heads[i],row[i]))
                i = i + 1
            print("""</owl:Class>""")
            print(i)

def subclassOf(row: list):
                    return"""
                    <owl:Class rdf:ID = sepal.length:"{0}"
                    <owl:Class rdf:ID = sepal.width:"{1}"
                    <owl:Class rdf:ID = petal.length:"{2}"
                    <owl:Class rdf:ID = petal.length:"{3}"
                <rdf:subClassOf rdf:resource="{4}"/>
                <owl:oneOf rdf:parseType="species">
                <owl:Thing rdf:about="#"/>
                 </owl:oneOf>
            </owl:Class>""" .format(row[0], row[1], row[2], row[3], row[4])

def individual(type: list):
    return"""
    <owl:Thing rdf:about="{0}"/>
    <owl:Thing rdf:about="{1}"/>
    <owl:Thing rdf:about="{2}"/>
    </owl:oneOf>
</owl:Class> """.format(type[0],type[1],type[2])

#Equivalence between Classes and properties

def equivalentClass():
    return """"
    <owl:Class rdf:ID="value">
  <owl:equivalentClass rdf:resource="&value1;value2"/>
</owl:Class>""".format()


#ENUMERATED CLASSES

def disjointclasses(column_heads: list):
                    return """
                    <owlx:DisjointClasses> 
                  <owlx:Class owlx:name="#{0}" />
                  <owlx:Class owlx:name="#{1}" />
                   <owlx:Class owlx:name="#{2}" />
                    <owlx:Class owlx:name="#{3}" />
                </owlx:DisjointClasses> """.format(column_heads[0],column_heads[1],column_heads[2],column_heads[3])
def addProperty(prop: list):
                return"""
            <owlx:ObjectProperty owlx:name="{}"> 
          <owlx:domain owlx:class="" /> 
          <owlx:range owlx:class="WineGrape" /> 
        </owlx:ObjectProperty>
        """.format(prop)

def functionalProperty(type: list):
    return """
    <owl:FunctionalProperty rdf:ID="type of species">

    <rdfs:domain>

      <owl:Class>

        <owl:unionOf rdf:parseType="speciies">

          <owl:Class rdf:about="#{0}"/>

          <owl:Class rdf:about="#{1}"/>

          <owl:Class rdf:about="#{2}"/>

        </owl:unionOf>

      </owl:Class>

    </rdfs:domain>

    <rdfs:range rdf:resource="http://www.w3.org/2001/XMLSchema#int"/>

    <rdf:type rdf:resource="http://www.w3.org/2002/07/owl#DatatypeProperty"/>

  </owl:FunctionalProperty>""".format(type[0],type[1],type[2])


