# -*- coding: utf-8 -*-
import sqlite3
conn = sqlite3.connect('D:/works/Project/green-robot-monitor/data/green_robot.db')
c = conn.cursor()
c.execute("SELECT name FROM sqlite_master WHERE type='table'")
tables = c.fetchall()
print('Tables:', tables)
for t in tables:
    name = t[0]
    c.execute(f'PRAGMA table_info({name})')
    cols = c.fetchall()
    print(f'\n=== {name} ===')
    for col in cols:
        print(f'  {col[1]} {col[2]}')
conn.close()
