# script to generate the SQL inserts from allbeers.csv
cat allbeers.csv | awk -F"," '

function fixquote(str1)
{
	gsub("'\''","'\'''\''",str1);
	return str1
}

{
 beer=fixquote($1);
 brewery=fixquote($2);
 bstyle=fixquote($3);
 abv=$4;
 country=$5;
 region=fixquote($6);
 ounces=$7;

 printf("INSERT INTO BEER ");
 printf("(name,brewery,country,region,abv,ibu,oz,style,discontinued,oos) VALUES ");
 printf("('\''%s'\'','\''%s'\'','\''%s'\'','\''%s'\'',%0.2f,null,%0.2f,'\''%s'\'',false,false);\n",
 	beer, brewery, country, region, abv, ounces, bstyle);

}'
