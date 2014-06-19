# Counting number of js libs
SELECT lib.name  , count(*) cc  
FROM js_business jbiz,js_lib lib 
where lib.id=jbiz.js_lib 
group by js_lib 
order by cc desc
