insert into public.user(id,
                        first_name,
                        second_name,
                        biography,
                        city,
                        birth_date,
                        password)
values ('8076b3bc-bfc7-458a-8d3d-c9c2e5436a83',
        'tester',
        'testers',
        'coder',
        'saint-p',
        '23.05.2010',
        '$2a$10$MWDDV6jP9a.PyRE6.8YMT.tMyzd0wsBEjZGgYTiC09L5imyQDDMl2'),
       ('8076b3bc-bfc7-458a-8d3d-c9c2e5436a84',
        'test',
        'testors',
        'coder',
        'saint-p',
        '23.05.2010',
        '$2a$10$MWDDV6jP9a.PyRE6.8YMT.tMyzd0wsBEjZGgYTiC09L5imyQDDMl2'),
       ('8076b3bc-bfc7-458a-8d3d-c9c2e5436a85',
        'test',
        'testors',
        'coder',
        'saint-p',
        '23.05.2010',
        '$2a$10$MWDDV6jP9a.PyRE6.8YMT.tMyzd0wsBEjZGgYTiC09L5imyQDDMl2');

insert into public.post(id,
                        author_id,
                        text,
                        created_at)
values ('8076b3bc-bfc7-458a-8d3d-c9c2e0000001', '8076b3bc-bfc7-458a-8d3d-c9c2e5436a83', 'Awesome post', '23.05.2010'),
       ('8076b3bc-bfc7-458a-8d3d-c9c2e0000002', '8076b3bc-bfc7-458a-8d3d-c9c2e5436a83', 'Awesome post2', '24.05.2010'),
       ('8076b3bc-bfc7-458a-8d3d-c9c2e0000003', '8076b3bc-bfc7-458a-8d3d-c9c2e5436a84', 'Awesome post3', '25.05.2010'),
       ('8076b3bc-bfc7-458a-8d3d-c9c2e0000004', '8076b3bc-bfc7-458a-8d3d-c9c2e5436a84', 'Awesome post4', '26.05.2010'),
       ('8076b3bc-bfc7-458a-8d3d-c9c2e0000005', '8076b3bc-bfc7-458a-8d3d-c9c2e5436a85', 'Awesome post5', '27.05.2010'),
       ('8076b3bc-bfc7-458a-8d3d-c9c2e0000006', '8076b3bc-bfc7-458a-8d3d-c9c2e5436a85', 'Awesome post6', '28.05.2010');

insert into public.friend_relation(user_id, friend_id)
values ('8076b3bc-bfc7-458a-8d3d-c9c2e5436a83', '8076b3bc-bfc7-458a-8d3d-c9c2e5436a84'),
       ('8076b3bc-bfc7-458a-8d3d-c9c2e5436a83', '8076b3bc-bfc7-458a-8d3d-c9c2e5436a85');