//findByNameContainingAndIsDeleted
SELECT * FROM category
WHERE name LIKE '%name%' AND is_deleted=0;

//findByNameAndCategory
SELECT * FROM banner
WHERE name='name' AND category_id=id;

//findByNameOrRequestId
SELECT * FROM category
WHERE name='name' OR request_id='requestId';