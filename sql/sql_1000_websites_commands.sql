SELECT * FROM smops_db.js_business;
select * from business where website='fabricexpressions.com';
SELECT website FROM smops_db.business order by rank;
select count(*) from business where num_pages > 0;
select count(*) from business;
select count(*) from business where num_pages > 0 and rank<=100;

select count(*) from business where num_pages > 0 and rank>=900;

#JS Library Queries
select count(*) cc, js_lib, lib.name from js_business jb, js_lib lib where lib.id=jb.js_lib group by js_lib order by cc desc; 
select count(distinct business) from js_business;
select rank,website,num_pages from business order by rank;
select count(*) from business where num_pages>0 and id not in (select business from js_business);
select count(*) from business where num_pages is not null;
select count(*) from business where server_type is not null;
select * from business where id=417;
select count(*) cc, business from js_business group by business order by cc desc;

#Form and Fields Queries
select count(distinct business) from 	form;
select count(*) from business where num_pages>0 and id in (select business from form);
select count(*) from business where num_pages>0 and id in (select business from form);
select count(distinct business) from form where purpose='UNKNOWN';
select field.info_type ,count(distinct business) cc  from form,field where field.form=form.id group by info_type order by cc desc ;
select field.type ,count(distinct business) cc  from form,field where field.form=form.id group by type order by cc desc ;
select *  from form,field where field.form=form.id and field.info_type='PHOTO' ;
select *  from form,field where field.form=form.id and field.type='file' ;
select count(distinct business) from form, field f1, field f2 where f1.form=form.id and f2.form=form.id and f1.info_type="EMAIL" and f2.info_type="PASSWORD"; 
select count(distinct business) from form, field f1, field f2 where f1.form=form.id and f2.form=form.id and f1.info_type="USERNAME" and f2.info_type="PASSWORD"; 
select count(distinct form.business) from form,field where field.form=form.id and field.info_type='EMAIL';
select count(distinct form.business) from form,field where field.form=form.id and field.info_type='NAME' and form.id not in 
( select form.id from form,field where field.form=form.id and field.info_type='PASSWORD' ) ;


select field.info_type ,count(distinct business) cc  from form,field, business biz where field.form=form.id and form.business=biz.id and rank>=900 group by info_type order by cc desc ;

#Server Type Queries
select distinct id, website, server_type from business where server_type is not null;
select distinct count(*) from business where num_pages>0 and server_type is not null;
select server_type, count(*) cc from business group by server_type order by cc desc;
select website, rank, server_type from business where server_type is not null order by rank;
select count(*) from business where rank<=100 and server_type like '%Apache%';
select count(*) from business where rank<=100 and server_type like '%Microsoft-IIS%';
select count(*) from business where rank<=100 and server_type like '%nginx%';
select count(*) from business where rank>=900 and server_type like '%Apache%';
select count(*) from business where rank>=900 and server_type like '%Microsoft-IIS%';
select count(*) from business where rank>=900 and server_type like '%nginx%';
select count(*) from business where server_type like '%Apache%';
select count(*) from business where server_type like '%Microsoft-IIS%';
select count(*) from business where server_type like '%nginx%';
select count(*) from business where server_type is not null;

#Page Types Queries
select count(*) from business where has_contact_us is not null;
select * from business where has_privacy_policy is not null;
select * from business where support_login is not null;

select * from business where support_login is not null;

#Trackers Queries
select count(distinct tracker_url) from tracker_business;
select tracker_url, count(*) cc from tracker_business group by tracker_url order by cc desc;
select count(distinct business) from tracker_business;
select business, count(*) cc from tracker_business group by business order by cc desc;
select business , website , rank , count(*) cc from tracker_business tr, business biz where tr.business=biz.id group by business order by rank;


#Seals Queries
select seal, count(*) cc from seal_business group by seal order by cc desc;
select * from seal_business where seal="MCAFEE";

#Website Types Queries
select website_type, has_contact_us, count(*) cc from business where num_pages>0 group by website_type, has_contact_us;

select website_type, has_privacy_policy, count(*) cc from business where num_pages>0 group by website_type, has_privacy_policy;

select rank, website, website_type from business where website_type is not null order by rank;

select count(*) from business where website_type is not null and website_type='OTHER' or website_type='POSTER' ;

select count(*) from business where website_type is not null and website_type='POSTER' and rank >= 900;
