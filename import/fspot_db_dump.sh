
dump_photos_sql="select p.id as photo_id, datetime(p.time, 'unixepoch') as date_shot, replace(p.base_uri, 'file:///home/math/Photos', '') || p.filename as path from photos p order by p.id;"
#dump_photo_versions_sql="select p.id as photo_id, datetime(p.time, 'unixepoch') as date_shot, replace(pv.base_uri, 'file:///home/math/Photos', '') || pv.filename as path from photos as p join photo_versions pv on pv.photo_id = p.id and p.default_version_id != pv.version_id order by p.time;"
dump_tags_sql="select t.id as tag_id, t.name as name, (case when t.category_id = 0 then null else t.category_id end) as parent_tag_id from tags as t order by t.id;"
dump_photo_tags_sql="select photo_id, tag_id from photo_tags;"

echo "Cleaning"
rm photos.dump
rm tags.dump
rm photos_tags.dump

echo "Dump photos"
sqlite3 ./photos.db "$dump_photos_sql" 1>photos.dump
#echo "Dump photo (other versions)"
#sqlite3 ./photos.db "$dump_photo_versions_sql" 1>photo_versions.dump
echo "Dump tags"
sqlite3 -nullvalue null ./photos.db "$dump_tags_sql" 1>tags.dump
echo "Dump photo/tag relationships"
sqlite3 ./photos.db "$dump_photo_tags_sql" 1>photos_tags.dump
