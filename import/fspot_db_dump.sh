
dump_photos_sql="select p.id as photo_id, datetime(p.time, 'unixepoch') as date_shot, (case when pv.base_uri is null then replace(p.base_uri, 'file:///home/math/Photos', '') else replace(pv.base_uri, 'file:///home/math/Photos', '') end) as path, (case when pv.filename is null then p.filename else pv.filename end) as filename from photos p left join photo_versions pv on pv.photo_id = p.id and pv.version_id = p.default_version_id order by p.id;"
dump_photo_versions_sql="select p.id as photo_id, datetime(p.time, 'unixepoch') as date_shot, (case when pv.base_uri is null then replace(p.base_uri, 'file:///home/math/Photos', '') else replace(pv.base_uri, 'file:///home/math/Photos', '') end) as path, (case when pv.filename is null then p.filename else pv.filename end) as filename from photos p join photo_versions pv on pv.photo_id = p.id and pv.version_id != p.default_version_id order by p.id;"
dump_tags_sql="select t.id as tag_id, t.name as name, (case when t.category_id = 0 then null else t.category_id end) as parent_tag_id from tags as t order by t.id;"
dump_photo_tags_sql="select photo_id, tag_id from photo_tags;"

echo "Dump photos"
sqlite3 $1/photos.db "$dump_photos_sql" 1>$1/photos.dump
echo "Dump photo (other versions)"
sqlite3 $1/photos.db "$dump_photo_versions_sql" 1>$1/photo_versions.dump
echo "Dump tags"
sqlite3 -nullvalue null $1/photos.db "$dump_tags_sql" 1>$1/tags.dump
echo "Dump photo/tag relationships"
sqlite3 $1/photos.db "$dump_photo_tags_sql" 1>$1/photos_tags.dump
