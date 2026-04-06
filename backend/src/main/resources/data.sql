/* 
Project requirement: at least 50 tuples in all relations 
*/


-- users: 4 PROVIDER, 12 ADOPTER 
INSERT INTO users (email, password, role) VALUES
    ('happypaws@shelter.com',     'password123', 'PROVIDER'),
    ('sunnyrescue@shelter.com',   'password123', 'PROVIDER'),
    ('pawsandclaws@rescue.com',   'password123', 'PROVIDER'),
    ('fureverhome@shelter.com',   'password123', 'PROVIDER'),
    ('alice@example.com',         'password123', 'ADOPTER'),
    ('bob@example.com',           'password123', 'ADOPTER'),
    ('carol@example.com',         'password123', 'ADOPTER'),
    ('david@example.com',         'password123', 'ADOPTER'),
    ('emma@example.com',          'password123', 'ADOPTER'),
    ('frank@example.com',         'password123', 'ADOPTER'),
    ('grace@example.com',         'password123', 'ADOPTER'),
    ('henry@example.com',         'password123', 'ADOPTER'),
    ('isabel@example.com',        'password123', 'ADOPTER'),
    ('james@example.com',         'password123', 'ADOPTER'),
    ('karen@example.com',         'password123', 'ADOPTER'),
    ('liam@example.com',          'password123', 'ADOPTER');

-- providers: user_id references PROVIDER users above (id 1-4)
INSERT INTO providers (user_id, org_name, phone, address, description, founded_year) VALUES
    (1, 'Happy Paws Shelter',    '555-0101', '12 Bark Avenue, Sydney',       'A no-kill shelter caring for dogs and cats.',         2005),
    (2, 'Sunny Rescue Centre',   '555-0202', '34 Whisker Lane, Melbourne',   'Specialising in small animals and rabbits.',          2011),
    (3, 'Paws and Claws Rescue', '555-0303', '56 Feline Street, Brisbane',   'Dedicated to rescuing strays and abandoned animals.', 2015),
    (4, 'Furever Home Shelter',  '555-0404', '78 Woof Road, Perth',          'Family-focused shelter with a strong adoption record.', 2008);

-- adopters: user_id references ADOPTER users above (id 5-16)
INSERT INTO adopters (user_id, first_name, last_name, phone, living_environment, household_size, has_children, has_other_pets, preferred_species) VALUES
    (5,  'Alice',  'Tan',     '555-1001', 'HOUSE',     3, TRUE,  FALSE, 'Dog'),
    (6,  'Bob',    'Lee',     '555-1002', 'APARTMENT', 1, FALSE, TRUE,  'Cat'),
    (7,  'Carol',  'Wong',    '555-1003', 'HOUSE',     4, TRUE,  TRUE,  'Dog'),
    (8,  'David',  'Chen',    '555-1004', 'APARTMENT', 2, FALSE, FALSE, 'Cat'),
    (9,  'Emma',   'Smith',   '555-1005', 'HOUSE',     5, TRUE,  FALSE, 'Dog'),
    (10, 'Frank',  'Brown',   '555-1006', 'APARTMENT', 1, FALSE, FALSE, 'Cat'),
    (11, 'Grace',  'Kim',     '555-1007', 'HOUSE',     3, TRUE,  TRUE,  'Dog'),
    (12, 'Henry',  'Park',    '555-1008', 'APARTMENT', 2, FALSE, TRUE,  'Cat'),
    (13, 'Isabel', 'Lim',     '555-1009', 'HOUSE',     4, TRUE,  FALSE, 'Dog'),
    (14, 'James',  'Ng',      '555-1010', 'APARTMENT', 1, FALSE, FALSE, 'Cat'),
    (15, 'Karen',  'Yap',     '555-1011', 'HOUSE',     3, TRUE,  TRUE,  'Dog'),
    (16, 'Liam',   'Nguyen',  '555-1012', 'APARTMENT', 2, FALSE, FALSE, 'Cat');

-- pets: spread across all 4 providers
INSERT INTO pets (provider_id, name, species, breed, age, temperament, description, photo_url, status) VALUES
    (1, 'Milo',    'Dog', 'Golden Retriever',   3, 'Friendly, Energetic',   'Loves to play fetch and go on long walks.',     'https://commons.wikimedia.org/wiki/Special:FilePath/Golden_retriever_retrieving_a_pheasant.jpg', 'AVAILABLE'),
    (1, 'Luna',    'Cat', 'Domestic Shorthair', 2, 'Calm, Affectionate',    'Enjoys lounging in sunny spots.',               'https://commons.wikimedia.org/wiki/Special:FilePath/Domestic_cat_felis_catus.jpg', 'AVAILABLE'),
    (1, 'Rocky',   'Dog', 'Labrador',           5, 'Loyal, Gentle',         'Great family dog, loves swimming.',             'https://commons.wikimedia.org/wiki/Special:FilePath/Labrador_retriever_bulaj1.jpg', 'AVAILABLE'),
    (1, 'Bella',   'Cat', 'Persian',            3, 'Quiet, Elegant',        'Prefers indoor life and gentle handling.',      'https://commons.wikimedia.org/wiki/Special:FilePath/Persian_in_Cat_Cafe.jpg', 'ADOPTED'),
    (2, 'Charlie', 'Dog', 'Poodle',             4, 'Intelligent, Gentle',   'Great with kids and other dogs.',               'https://commons.wikimedia.org/wiki/Special:FilePath/Pudel_kleinweiss.jpg', 'AVAILABLE'),
    (2, 'Daisy',   'Cat', 'Siamese',            1, 'Curious, Vocal',        'Very social and loves attention.',              'https://commons.wikimedia.org/wiki/Special:FilePath/Siamese_cat.jpg', 'AVAILABLE'),
    (2, 'Max',     'Dog', 'Beagle',             2, 'Playful, Curious',      'Loves sniffing around and exploring.',          'https://commons.wikimedia.org/wiki/Special:FilePath/Beagle_portrait_Camry.jpg', 'AVAILABLE'),
    (2, 'Cleo',    'Cat', 'Ragdoll',            4, 'Docile, Affectionate',  'Goes limp when held, very relaxed.',            'https://commons.wikimedia.org/wiki/Special:FilePath/Pansy_cat.jpg', 'ON_HOLD'),
    (3, 'Buddy',   'Dog', 'Border Collie',      3, 'Active, Smart',         'Needs lots of exercise and mental stimulation.','https://commons.wikimedia.org/wiki/Special:FilePath/Short_Haired_Border_Collie.jpg', 'AVAILABLE'),
    (3, 'Mia',     'Cat', 'Maine Coon',         2, 'Gentle, Sociable',      'Gets along well with everyone.',                'https://commons.wikimedia.org/wiki/Special:FilePath/Maine_coon_profile.jpg', 'AVAILABLE'),
    (3, 'Oscar',   'Dog', 'Corgi',              4, 'Cheerful, Loyal',       'Short legs, big personality.',                  'https://commons.wikimedia.org/wiki/Special:FilePath/Kelsey_n_Penny_%28Welsh_Corgis%29.jpg', 'AVAILABLE'),
    (4, 'Ruby',    'Dog', 'Shih Tzu',           5, 'Calm, Affectionate',    'Loves cuddles and short walks.',                'https://commons.wikimedia.org/wiki/Special:FilePath/Shih-Tzu.jpg', 'AVAILABLE'),
    (4, 'Leo',     'Cat', 'British Shorthair',  3, 'Reserved, Calm',        'Independent but affectionate on his terms.',    'https://commons.wikimedia.org/wiki/Special:FilePath/Britskorthaar-64091287828362D7bA.jpg', 'AVAILABLE'),
    (4, 'Molly',   'Dog', 'Dalmatian',          2, 'Energetic, Playful',    'Needs an active owner for daily runs.',         'https://commons.wikimedia.org/wiki/Special:FilePath/Dalmatian_puppy.JPG', 'AVAILABLE'),
    (4, 'Nala',    'Cat', 'Abyssinian',         1, 'Active, Curious',       'Always on the move, loves to climb.',           'https://commons.wikimedia.org/wiki/Special:FilePath/Valentino.jpg', 'AVAILABLE');

-- messages: sender/receiver are user ids, pet_id references pets table
INSERT INTO messages (sender_id, receiver_id, pet_id, content, is_read) VALUES
    (5,  1, 1,  'Hi, is Milo still available for adoption?',                    TRUE),
    (1,  5, 1,  'Yes, Milo is available! Would you like to schedule a visit?',  TRUE),
    (5,  1, 1,  'That would be great, how about this Saturday?',                FALSE),
    (6,  2, 6,  'Is Daisy good with other cats?',                               TRUE),
    (2,  6, 6,  'Yes, Daisy gets along well with other animals.',               FALSE),
    (7,  1, 3,  'Can you tell me more about Rocky?',                            TRUE),
    (1,  7, 3,  'Rocky is 5 years old and loves swimming. Very gentle dog.',    FALSE),
    (8,  3, 9,  'Is Buddy suitable for apartment living?',                      TRUE),
    (3,  8, 9,  'Buddy needs space and exercise, a house with a yard is ideal.', FALSE),
    (9,  4, 12, 'How old is Ruby and is she house trained?',                    TRUE),
    (4,  9, 12, 'Ruby is 5 years old and fully house trained.',                 FALSE),
    (10, 2, 5,  'I would like to adopt Charlie. What is the process?',          TRUE);