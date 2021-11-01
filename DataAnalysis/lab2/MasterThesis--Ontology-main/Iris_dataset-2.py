#!/usr/bin/env python
# coding: utf-8

# Data Preprocessing :

# In[23]:


import pandas as pd
df = pd.read_csv('iris.csv')
df.head()
df_10=pd.read_csv('iris.csv')


# In[2]:


#saving all the attributes in the form of lists

dataframe_float=df.select_dtypes(include ='float64') 
dataframe_float_list=list(dataframe_float.columns)
print("column list with nominal values :" '\n')
print(dataframe_float_list)

dataframe_text=df.select_dtypes(exclude ='float64') 
dataframe_text_list=list(dataframe_text.columns)
print( '\n' "column list with numerical values :" '\n')
print(dataframe_text_list)


# In[3]:


column_heads=list(df)
print(column_heads)


# In[4]:


type=df["species"].unique().tolist()
print(type)


# Subsetting the part of the dataframe containing numeric datatypes

# In[5]:


dataframe_numeric= df.loc[:, 'sepal_length':'petal_width']

               #columns =['sepal_length', 'sepal_width','petal_length','petal_width'])
dataframe_numeric.head()


# Max numerical value among all the features

# In[6]:


import numpy as numpy
max_value=list(dataframe_numeric.max())
print(max_value)


# In[7]:


cardinality= list(dataframe_numeric.shape)
print(cardinality)


# Calculating the Pearson Correlation Coefficient :

# In[8]:



print("Correlation Matrix")
correlation_mat = df.corr(method='pearson')
print(correlation_mat)
print()

def get_redundant_pairs(df):
    '''Get diagonal and lower triangular pairs of correlation matrix'''
    pairs_to_drop = set()
    cols = df.columns
    for i in range(0, df.shape[1]):
        for j in range(0, i+1):
            pairs_to_drop.add((cols[i], cols[j]))
    return pairs_to_drop

def get_top_abs_correlations(df, n=5):
    au_corr = df.corr().abs().unstack()
    labels_to_drop = get_redundant_pairs(df)
    au_corr = au_corr.drop(labels=labels_to_drop).sort_values(ascending=False)
    return au_corr[0:n]

print("Top Absolute Correlations")
print(get_top_abs_correlations(dataframe_numeric,10))


# Sorting the correlation matrix
# If the given data has a large number of features, the correlation matrix can become very big 
# and hence difficult to interpret.
# Sometimes we might want to sort the values in the matrix and see the strength of correlation 
# between various feature pairs in an increasing or decreasing order.
# First, we will convert the given matrix into a one-dimensional Series of values.

# In[9]:


corr_pairs = correlation_mat.unstack().sort_values(kind="quicksort")
print('\n', corr_pairs)


# In[10]:


df_corr= corr_pairs.to_frame()
dist_df = df_corr.reset_index(level=[0,1])
attribute_df=dist_df.rename(columns={"level_0": "attribute_One", "level_1": "attribute_Two",0:"Coeff"})
print(attribute_df)


# In[11]:


print (attribute_df['attribute_One'] != attribute_df['attribute_Two']
      )


# removing the rows where attribute_One and attribute_Two matches
# 
# 

# In[12]:


attribute_df = attribute_df.query("attribute_One != attribute_Two")
print(attribute_df)


# removing repeated rows 

# In[13]:


result_df = attribute_df.drop_duplicates(subset=['Coeff'], keep='first')
print(result_df)


# When the threshold is more than a certain coefficient value:

# In[14]:


#p-value
threshold = 0.5 


# In[15]:


#if the p-value is more than 0.5 coeff
threshold_df= result_df[result_df.Coeff > threshold]
print(threshold_df)


# Result of the second attribute after the threshold value and the entered attribute is taken into account 

# In[17]:


feature_extraction_df=threshold_df.loc[result_df['attribute_One'] == column_heads[2]]
print(feature_extraction_df)
secondAttribute = list(feature_extraction_df['attribute_Two'])
print('\n','Attribute result when one attribute is entered and a related attribute is asked for ' "\n")

print(secondAttribute)


# Result of the second attribute without considering the threshold value .

# In[18]:


feature_extraction_df=result_df.loc[result_df['attribute_One'] == column_heads[0]]
print(feature_extraction_df)
secondAttribute = list(feature_extraction_df['attribute_Two'])
print('\n','attribute result when one attribute is entered and a related attribute is asked for ' "\n")

print(secondAttribute)


# Parsing file from csv to ontology including all the preprocessed features :

# In[19]:


#species as class 
#max cardinality value as per the dataset
def speciesclass():
    return ("""<owl:Class rdf:about="species"> \n
    <rdfs:subClassOf rdf:resource="&Iris;Flower"/> \n
            <rdfs:subClassOf> \n
                <owl:Restriction> \n
                    <owl:maxCardinality rdf:datatype="&xsd;nonNegativeInteger">{0}</owl:maxCardinality> \n
                    <owl:onProperty rdf:resource="&features;"/> \n
                </owl:Restriction> \n
            </rdfs:subClassOf> \n
</owl:Class> """).format(cardinality[0])
print(speciesclass())


# In[20]:


#attributes about species
#Define properties
#datatype property 


def attr(column_heads):
                    return """
                    <owl:ObjectProperty rdf:ID="flower"> 
  <rdfs:domain rdf:resource={3}/>
  <rdfs:range rdf:resource={0}/>  
</owl:ObjectProperty> """.format(column_heads[0],column_heads[1],column_heads[2],column_heads[3])
    
#print(attr(column_heads))

def individual(type):
    return"""
    <owl:Thing rdf:about="{0}"/>
    <owl:Thing rdf:about="{1}"/>
    <owl:Thing rdf:about="{2}"/>
    </owl:oneOf>
</owl:Class> """.format(type[0],type[1],type[2])
#print(individual(type))

#Equivalence between Classes and properties

def equivalentClass():
    return """"
    <owl:Class rdf:ID="value">
  <owl:equivalentClass rdf:resource="&value1;value2"/>
</owl:Class>""".format()


#ENUMERATED CLASSES

def disjointclasses(column_heads):
                    return """
                    <owlx:DisjointClasses> 
                  <owlx:Class owlx:name="#{0}" />
                  <owlx:Class owlx:name="#{1}" />
                   <owlx:Class owlx:name="#{2}" />
                    <owlx:Class owlx:name="#{3}" />
                </owlx:DisjointClasses> """.format()

#Complex Classes 
#Set Operators
def intersection():
    return """<owl:Class rdf:ID="entervalue">
  <owl:intersectionOf rdf:parseType="#Collection">
    <owl:Class rdf:about="#Wine" />
    <owl:Restriction>
      <owl:onProperty rdf:resource="#property" />
      <owl:hasValue rdf:resource="#" />
    </owl:Restriction>
  </owl:intersectionOf>
</owl:Class>"""
   
    
def union():
    return """
    <owl:Class rdf:ID="superclass">
  <owl:unionOf rdf:parseType="Collection">
    <owl:Class rdf:about="#subclass" />
    <owl:Class rdf:about="#subclass" />
  </owl:unionOf>
</owl:Class>"""

def complement():
    return """

 <owl:Class rdf:ID= {0} />
  <owl:Class rdf:ID={1}>
    <owl:complementOf rdf:resource= {0} />
    <owl:Class rdf:ID={2}>
    <owl:complementOf rdf:resource={0} />
    <owl:Class rdf:ID={3}>
    <owl:complementOf rdf:resource={0} />
    
  </owl:Class>""" .format(column_heads[0],secondAttribute[0],secondAttribute[1],secondAttribute[2])
#print(complement())


# In[24]:


#converting my original dataframe into nested list so that each rows are parsed

listed_dataframe=df.values.tolist() 
listed_dataframe_one=df_10.values.tolist()


# In[25]:


#Instance Data

def property(row):
    for row in listed_dataframe_one:
        if row !=listed_dataframe_one[0]:
            i = 0
            for prop in row :
                print("""
                <species:Species rdf:about="#Features">
                    <species:feature rdf:ID = "{0}">{1} </species:feature>
                    
            """ .format(column_heads[i],row[i]))
                i = i + 1

            print("""</owl:Class>""")
            #print(i)
#print (property(True)) 


# In[26]:


starting_Text=("<rdf:RDF>" '\n'
"xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#" '\n'
      "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#" '\n'
      "xmlns:owl=\"http://www.w3.org/2002/07/owl#" '\n'
      "xmlns:dc=\"http://purl.org/dc/elements/1.1/" '\n'
       "xmlns:iris=\"http://www.w3.org/iris#>" '\n'
       )

#owl header"
owlheader=("<owl:Ontology rdf:about=\"http://www.datatools.com/irisdataset>" '\n'
"<dc:title>The iris dataset Ontology</dc:title>" '\n'
"<dc:description>An ontology construction in python</dc:description>" '\n'
"</owl:Ontology>" '\n')

end_Rdf=('\n'"</rdf:RDF>")


# In[27]:


def ontology():
    print(starting_Text,owlheader)
    print(speciesclass())
    print(attr(column_heads))
    print(individual(type))
    print(complement())
    for row in listed_dataframe:
                    print(property(row))
                


# In[31]:


print(ontology()) 


# output of file that is generated

# In[ ]:


#output of file that is generated

import sys
file = open('irisfile.owl', 'w+')
sys.stdout = file
print(ontology(),end_Rdf)    
file.close()

