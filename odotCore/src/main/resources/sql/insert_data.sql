-- * Copyright 2017 Makoto Consulting Group, Inc.
-- * 
-- * Licensed under the Apache License, Version 2.0 (the "License");
-- * you may not use this file except in compliance with the License.
-- * You may obtain a copy of the License at
-- * 
-- * http://www.apache.org/licenses/LICENSE-2.0
-- * 
-- * Unless required by applicable law or agreed to in writing, software
-- * distributed under the License is distributed on an "AS IS" BASIS,
-- * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- * See the License for the specific language governing permissions and
-- * limitations under the License.
INSERT INTO odot_category(name, description, when_created) VALUES('TEST_CATEGORY1', 'TEST_DESCRIPTION1', CURRENT_DATE);
INSERT INTO odot_category(name, description, when_created) VALUES('TEST_CATEGORY2', 'TEST_DESCRIPTION2', CURRENT_DATE);
INSERT INTO odot_category(name, description, when_created) VALUES('TEST_CATEGORY3', 'TEST_DESCRIPTION3', CURRENT_DATE);
INSERT INTO odot_category(name, description, when_created) VALUES('TEST_CATEGORY4', 'TEST_DESCRIPTION4', CURRENT_DATE);

INSERT INTO odot_item(description, due_date, finished, category_id) VALUES('TODO Item #1', CURRENT_DATE, false, (SELECT id FROM odot_category WHERE name = 'TEST_CATEGORY1'));
INSERT INTO odot_item(description, due_date, finished, category_id) VALUES('TODO Item #2', CURRENT_DATE, false, (SELECT id FROM odot_category WHERE name = 'TEST_CATEGORY2'));
INSERT INTO odot_item(description, due_date, finished, category_id) VALUES('TODO Item #3', CURRENT_DATE, false, (SELECT id FROM odot_category WHERE name = 'TEST_CATEGORY3'));
INSERT INTO odot_item(description, due_date, finished, category_id) VALUES('TODO Item #4', CURRENT_DATE, false, (SELECT id FROM odot_category WHERE name = 'TEST_CATEGORY4'));
INSERT INTO odot_item(description) VALUES('TODO Item #5');  
INSERT INTO odot_item(description, due_date) VALUES('TODO Item #6', CURRENT_DATE);  
INSERT INTO odot_item(description, due_date, finished) VALUES('TODO Item #7', CURRENT_DATE, false);  
  
