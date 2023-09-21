from create import Create
from tabulate import tabulate
from model import Model
from process import Process
import pandas as pd

# c = Create(5)
#
# # proc = Process(5)
# proc = Process(5,2)
#
# proc.max_queue = 5
#
# c.distribution = 'exp'
# proc.distribution = 'exp'
#
# c.name = 'Creator'
# proc.name = 'Process 1'
#
# c.next_element = [proc]
#
# elements = [c, proc]
# model = Model(elements)
# res = model.simulate(1000)

# c = Create(5)
#
# p1 = Process(5)
# p2 = Process(5)
# p3 = Process(5)
#
# c.next_element = [p1]
# p1.next_element = [p2, p3]
#
# p1.probability = ([0.5, 0.5])
#
# p1.max_queue = 5
# p2.max_queue = 5
# p3.max_queue = 5
#
# c.distribution = 'exp'
# p1.distribution = 'exp'
# p2.distribution = 'exp'
# p3.distribution = 'exp'
#
# c.name = 'Creator'
# p1.name = 'Process 1'
# p2.name = 'Process 2'
# p3.name = 'Process 3'
#
# elements = [c, p1, p2, p3]
# model = Model(elements)
# res = model.simulate(1000)


n_param = 15

delay_create_list =   [2, 10, 1, 6, 7, 8, 1, 3, 0.5, 5, 5, 4, 4, 4, 4]
delay_process1_list = [2, 1, 5, 4, 4, 5, 0.7, 0.4, 4, 1, 4, 4, 4, 0.3, 4]
delay_process2_list = [2, 1, 4, 4, 4, 4, 4, 4, 6, 4, 0.5, 4, 4, 4, 4]
delay_process3_list = [2, 1, 4, 4, 10, 4, 4, 4, 4, 4, 4, 0.7, 4, 4, 4]
maxQ_list1 =          [5, 4, 5, 5, 5, 15, 5, 9, 6, 6, 5, 7, 1, 5, 5]
maxQ_list2 =          [5, 3, 7, 4, 5, 8, 1, 5, 5, 6, 7, 5, 8, 1, 5]
maxQ_list3 =          [5, 5, 5, 8, 5, 6, 5, 10, 5, 6, 5, 5, 5, 5, 1]
distribution = 'exp'

df = pd.DataFrame()
rows = []

for i in range(n_param):
    c = Create(delay_create_list[i])

    proc1 = Process(delay_process1_list[i])
    proc2 = Process(delay_process2_list[i])
    proc3 = Process(delay_process3_list[i])

    proc1.max_queue = maxQ_list1[i]
    proc2.max_queue = maxQ_list2[i]
    proc3.max_queue = maxQ_list3[i]

    c.distribution = distribution
    proc1.distribution = distribution
    proc2.distribution = distribution
    proc3.distribution = distribution

    c.name = 'Creator'
    proc1.name = 'Process 1'
    proc2.name = 'Process 2'
    proc3.name = 'Process 3'

    c.next_element = [proc1]
    proc1.next_element = [proc2]
    proc2.next_element = [proc3]

    elements = [c, proc1, proc2, proc3]
    model = Model(elements)
    res = model.simulate(1000)

    param = {'delay_create': delay_create_list[i],
             'delay_process1': delay_process1_list[i],
             'delay_process2': delay_process2_list[i],
             'delay_process3': delay_process3_list[i],
             'max_queue1': maxQ_list1[i],
             'max_queue2': maxQ_list2[i],
             'max_queue3': maxQ_list3[i],
             'process1_processed': proc1.quantity,
             'process1_failed': proc1.failure,
             'process2_processed': proc2.quantity,
             'process2_failed': proc2.failure,
             'process3_processed': proc3.quantity,
             'process3_failed': proc3.failure,
             'distribution': distribution}

    rows.append({**param, **res})

# назва файлу xlsx
file_name = 'ModelData.xlsx'

# імпорт в Excel
df = df._append(rows)
df.to_excel(file_name)
print(tabulate(df, headers='keys', tablefmt='fancy_grid', numalign="center"))