insert into dialog(id, from_id, to_id)
values ('00000000-0000-0000-0000-000000000001',
        '00000000-0000-0000-0000-000000000002',
        '8076b3bc-bfc7-458a-8d3d-c9c2e5436a83');
insert into message(id, dialog_id, from_id, to_id, text, created_at)
values ('00000000-0000-0000-0000-000000000001',
        '00000000-0000-0000-0000-000000000001',
        '00000000-0000-0000-0000-000000000002',
        '8076b3bc-bfc7-458a-8d3d-c9c2e5436a83',
        'hi there',
        '23.05.2010'),
       ('00000000-0000-0000-0000-000000000002',
        '00000000-0000-0000-0000-000000000001',
        '8076b3bc-bfc7-458a-8d3d-c9c2e5436a83',
        '00000000-0000-0000-0000-000000000002',
        'hello',
        '24.05.2010');