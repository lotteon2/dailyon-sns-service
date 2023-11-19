-- Dummy data for the member table
INSERT INTO member (id, nickname, profile_img_url, code, following_count, follower_count)
VALUES
    (1, 'JohnDoe', '/member/john-doe-profile.png', 'JD001', 20, 15),
    (2, 'AliceSmith', '/member/alice-smith-profile.png', 'AS002', 25, 18),
    (3, 'BobJohnson', '/member/bob-johnson-profile.png', 'BJ003', 15, 22);

-- Dummy data for the follow table
INSERT INTO follow (follower_id, following_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 1),
    (3, 2);

-- Dummy data for the post table
INSERT INTO post (created_at, updated_at, member_id, title, `description`, stature, weight, view_count, like_count, comment_count, is_deleted)
VALUES
    (NOW(), NOW(), 1, 'First Post', 'Description for the first post.', 180.5, 75.0, 100, 35, 12, 0),
    (NOW(), NOW(), 2, 'Awesome Journey', 'Exciting travel adventure.', 160.0, 55.5, 120, 45, 20, 0),
    (NOW(), NOW(), 3, 'Fitness Goals', 'Working towards fitness goals.', 175.0, 80.0, 80, 28, 15, 0),
    (NOW(), NOW(), 1, 'Another Post', 'Description for another post.', 170.0, 70.0, 90, 40, 18, 0),
    (NOW(), NOW(), 2, 'Memorable Trip', 'Memories from a wonderful trip.', 150.0, 60.0, 110, 38, 16, 0),
    (NOW(), NOW(), 1, 'Sunset Paradise', 'Enjoying a beautiful sunset.', 155.0, 65.0, 95, 42, 14, 0),
    (NOW(), NOW(), 2, 'Tech Innovations', 'Exploring the latest tech innovations.', 165.0, 70.0, 120, 55, 25, 0),
    (NOW(), NOW(), 3, 'Healthy Recipes', 'Sharing some delicious and healthy recipes.', 175.0, 75.0, 80, 30, 12, 0),
    (NOW(), NOW(), 1, 'City Lights', 'Admiring the city lights at night.', 160.0, 68.0, 110, 48, 20, 0),
    (NOW(), NOW(), 2, 'Wildlife Adventure', 'Encounters with wildlife in the jungle.', 180.0, 85.0, 100, 40, 18, 0),
    (NOW(), NOW(), 3, 'Gardening Tips', 'Tips for maintaining a beautiful garden.', 150.0, 60.0, 90, 35, 15, 0),
    (NOW(), NOW(), 1, 'Mountain Trek', 'Scaling new heights in the mountains.', 170.0, 78.0, 130, 60, 28, 0),
    (NOW(), NOW(), 2, 'Ocean Exploration', 'Diving into the depths of the ocean.', 160.0, 70.0, 105, 45, 22, 0),
    (NOW(), NOW(), 3, 'Artistic Creations', 'Showcasing some artistic creations.', 155.0, 65.0, 85, 38, 16, 0),
    (NOW(), NOW(), 1, 'Snowy Adventure', 'Braving the snow in a winter adventure.', 175.0, 82.0, 95, 42, 18, 0),
    (NOW(), NOW(), 2, 'Culinary Delights', 'Exploring the world of culinary delights.', 165.0, 75.0, 120, 50, 24, 0);

-- Dummy data for the post_image table
INSERT INTO post_image (created_at, updated_at, post_id, thumbnail_img_url, img_url)
VALUES
    (NOW(), NOW(), 1, '/post/thumbnail1.png', '/post/image1.png'),
    (NOW(), NOW(), 2, '/post/thumbnail2.png', '/post/image2.png'),
    (NOW(), NOW(), 3, '/post/thumbnail3.png', '/post/image3.png'),
    (NOW(), NOW(), 4, '/post/thumbnail4.png', '/post/image4.png'),
    (NOW(), NOW(), 5, '/post/thumbnail5.png', '/post/image5png'),
    (NOW(), NOW(), 6, '/post/thumbnail6.png', '/post/image6.png'),
    (NOW(), NOW(), 7, '/post/thumbnail7.png', '/post/image7.png'),
    (NOW(), NOW(), 8, '/post/thumbnail8.png', '/post/image8.png'),
    (NOW(), NOW(), 9, '/post/thumbnail9.png', '/post/image9.png'),
    (NOW(), NOW(), 10, '/post/thumbnail10.png', '/post/image10.png'),
    (NOW(), NOW(), 11, '/post/thumbnail11.png', '/post/image11.png'),
    (NOW(), NOW(), 12, '/post/thumbnail12.png', '/post/image12.png'),
    (NOW(), NOW(), 13, '/post/thumbnail13.png', '/post/image13.png'),
    (NOW(), NOW(), 14, '/post/thumbnail14.png', '/post/image14.png'),
    (NOW(), NOW(), 15, '/post/thumbnail15.png', '/post/image15.png'),
    (NOW(), NOW(), 16, '/post/thumbnail16.png', '/post/image16.png');

-- Dummy data for the post_image_product_detail table
INSERT INTO post_image_product_detail (created_at, updated_at, post_image_id, product_id, product_size, left_gap_percent, top_gap_percent)
VALUES
    (NOW(), NOW(), 1, 101, 'Large', 0.1, 0.2),
    (NOW(), NOW(), 2, 102, 'Medium', 0.05, 0.1),
    (NOW(), NOW(), 3, 103, 'Small', 0.2, 0.15),
    (NOW(), NOW(), 4, 101, 'Large', 0.1, 0.2),
    (NOW(), NOW(), 5, 102, 'Medium', 0.05, 0.1),
    (NOW(), NOW(), 6, 103, 'Small', 0.2, 0.15),
    (NOW(), NOW(), 7, 101, 'Large', 0.1, 0.2),
    (NOW(), NOW(), 8, 102, 'Medium', 0.05, 0.1),
    (NOW(), NOW(), 9, 103, 'Small', 0.2, 0.15),
    (NOW(), NOW(), 10, 101, 'Large', 0.1, 0.2),
    (NOW(), NOW(), 11, 102, 'Medium', 0.05, 0.1),
    (NOW(), NOW(), 12, 103, 'Small', 0.2, 0.15),
    (NOW(), NOW(), 13, 101, 'Large', 0.1, 0.2),
    (NOW(), NOW(), 14, 102, 'Medium', 0.05, 0.1),
    (NOW(), NOW(), 15, 103, 'Small', 0.2, 0.15),
    (NOW(), NOW(), 16, 101, 'Large', 0.1, 0.2);

-- Dummy data for the post_like table
INSERT INTO post_like (member_id, post_id)
VALUES
    (1, 2),
    (2, 3),
    (3, 1);

-- Dummy data for the comment table
INSERT INTO comment (created_at, updated_at, parent_id, member_id, post_id, description)
VALUES
    (NOW(), NOW(), NULL, 1, 2, 'Great post!'),
    (NOW(), NOW(), NULL, 2, 3, 'Keep up the good work!'),
    (NOW(), NOW(), 1, 1, 3, 'I agree with you.');

-- Dummy data for the hash_tag table
INSERT INTO hash_tag (name, created_at, updated_at, post_id)
VALUES
    ('travel', NOW(), NOW(), 2),
    ('fitness', NOW(), NOW(), 3),
    ('adventure', NOW(), NOW(), 1);
