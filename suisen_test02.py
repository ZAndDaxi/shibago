import numpy as np
import time

n_days = 100  # Number for days
n_time = int(24 * 60/10) # Number for samples of position per day, 10 minutes
d_time = 2 #day, minituse
n_position = 5 # Number of positions that user have gone have gone
d_position = 2 # x,y for two
n_fetures = 4 #Study, Meat, Vegetable, Sports

def make_p1():
    p1_1 = [0.8, 0.1, 0, 0, 0.1]
    p1_2 = [0.2, 0.4, 0.2, 0, 0.2]
    p1_3 = [0.2, 0.4, 0.3, 0, 0.1]
    p1_4 = [0.2, 0.2, 0.3, 0.1, 0.2]
    p1 = [p1_1, p1_2, p1_3, p1_4]
    return p1

def make_p2():
    p2_1 = [0.8, 0.1, 0, 0, 0.1]
    p2_2 = [0.2, 0.4, 0, 0.2, 0.2]
    p2_3 = [0.2, 0.4, 0, 0.3, 0.1]
    p2_4 = [0.2, 0.2, 0.1, 0.3, 0.2]
    p2 = [p2_1, p2_2, p2_3, p2_4]
    return p2

def make_positions():
    pos =np.zeros(shape = (n_position, d_position))
    for n_pos in range(pos.shape[0]):
        pos[n_pos] = np.random.randint(0, 100, d_position)
    return pos

def make_place():
    ID = ['0001', '0002', '0003', '0004', '0005']
    index_ls = ['Home', 'School', 'Meat_ya', 'Vegetable_ya', 'Gym']
    Home_label = [0.2, 0.2, 0.2, 0.2] #Study, Meat, Vegetable, Sports
    School_label = [0.1, 0.2, 0.2, 0.5]
    Meat_ya_label = [0, 1, 0.1, 0]
    Vegetable_ya_label = [0, 0.1, 1, 0]
    Gym_label = [0, 0, 0, 0, 1]
    place_label_all = [Home_label, School_label, Meat_ya_label, Vegetable_ya_label, Gym_label]
    position = make_positions()
    return ID, index_ls, place_label_all, position


# Simulation of behavioral patterns
def make_data_recoder(positions, p):
    data_recorder = np.zeros(shape=(n_days, n_time, d_position))
    time_recorder = np.zeros(shape=(n_days, n_time, d_time))
    for d_0 in range(data_recorder.shape[0]):
        for d_1 in range(data_recorder.shape[1]):
            time_recorder[d_0, d_1] = [int(d_0), int(d_1)]

            if 0 < d_1 < data_recorder.shape[1] / 4:
                a = np.random.choice(n_position, p=p[0])
                data_recorder[d_0, d_1] = positions[a] + np.random.randn(d_position)
            elif data_recorder.shape[1] / 4 < d_1 < data_recorder.shape[1] / 2:
                a = np.random.choice(n_position, p=p[1])
                data_recorder[d_0, d_1] = positions[a] + np.random.randn(d_position)
            elif data_recorder.shape[1] / 2 < d_1 < data_recorder.shape[1] * 3 / 4:
                a = np.random.choice(n_position, p=p[2])
                data_recorder[d_0, d_1] = positions[a] + np.random.randn(d_position)
            elif data_recorder.shape[1] * 3 / 4 < d_1 < data_recorder.shape[1]:
                a = np.random.choice(n_position, p=p[3])
                data_recorder[d_0, d_1] = positions[a] + np.random.randn(d_position)
            else:
                data_recorder[d_0, d_1] = positions[np.random.randint(0, n_position)] + np.random.randn(d_position)
    return time_recorder, data_recorder


"""
class place_infor:
从数据库中读取所有地点信息
各个要素信息
place_ID：字符串型，例如 '0001'
place_Name：字符串型，例如'Home'
place_feature：储存浮点小数的list，例如 [0.2, 0.2, 0.2, 0.2]
place_pos：每个地点对应的int型（或者浮点型）坐标，例如（10, 10）
"""
class place_infor:
    def __init__(self, place_ID, place_Name, place_feature, place_pos):
        self.ID = place_ID
        self.Name = place_Name
        self.feature = place_feature
        self.place_pos = place_pos

"""
读取一个确定的user的信息
各个要素信息
ID：userID，字符串型，例如'0000001'
time_recoder：例如过去100天每十分钟记录的数据的对应时间，这里对应一个np数组，形状为(100, 144, 2)，···储存周几的信息（0，1，2，…6）以及每天的时间（int((hour * 60 + minutes) / 10)）
data_recoder：例如过去100天每十分钟记录的数据的对应坐标，这里对应一个np数组，形状为(100, 144, 2)，··储存经纬度两个数值
positions：输入每个地点的坐标，用于返回每个时间对应的地点名称
"""
class users:
    def __init__(self, userID, time_recoder, data_recorder, positions):
        self.ID = userID
        self.time_recoder = time_recoder
        self.data_recoder = data_recorder
        self.recoder_name = self.get_name(data_recorder, positions)

    # Get name from the position
    def get_name(self, data_recorder, positions):
        data_recoder_name = np.zeros(shape=[data_recorder.shape[0], data_recorder.shape[1]])
        for i in range(data_recorder.shape[0]):
            for j in range(data_recorder.shape[1]):
                p_to_p_disance = np.sqrt(np.sum(np.square(positions[0] - data_recorder[i, j])))
                pos = 0
                for k in range(positions.shape[0] - 1):
                    new_p_to_p_disance = np.sqrt(np.sum(np.square(positions[k + 1] - data_recorder[i, j])))
                    if new_p_to_p_disance < p_to_p_disance:
                        p_to_p_disance = new_p_to_p_disance
                        pos = k + 1
                data_recoder_name[i, j] = pos
        return data_recoder_name

"""
用于推荐系统，读取过去的记录，来进行推荐
各个要素信息
time_recoder：例如过去100天每十分钟记录的数据的对应时间，这里对应一个np数组，形状为(100, 144, 2)，···储存周几的信息（0，1，2，…6）以及每天的时间（int((hour * 60 + minutes) / 10)）
local_time：从计算机系统读取时间
data_recoder_name：例如过去100天每十分钟记录的地点信息
suggest_place：返回推荐的int类型数值
"""
class recommend_system:
    def __init__(self, time_recoder, data_recoder_name):
        self.time_recoder = time_recoder
        self.local_time = time.localtime(time.time())
        self.data_recoder_name = data_recoder_name
        self.day, self.minutes= self.get_time()
        self.suggest_place = self.recommend()

    def get_time(self):
        day = self.local_time.tm_wday
        hour = self.local_time.tm_hour
        minutes = self.local_time.tm_min
        time = self.make_time(hour, minutes)
        return day, time

    def make_time(self, hour, minutes):
        return int((hour * 60 + minutes) / 10)

    def recommend(self):
        data = self.data_recoder_name[:, self.minutes]
        p = np.zeros(n_position)
        for i in range(n_position):
            p[i] = np.sum(data == i)
        p = p / np.sum(p)

        if self.day%7 < 6:
            suggest = np.random.choice(n_position, p=p)

        else:
            p = np.exp(-p)
            p = p / np.sum(p)
            suggest = np.random.choice(n_position, p=p)

        return suggest


def main():

    #Test_data below
    p1 = make_p1()
    p2 = make_p2()
    ID, index_ls, place_label_all, place_pos = make_place()

    time_recoder01, data_recoder01 = make_data_recoder(place_pos, p1)
    time_recoder02, data_recoder02 = make_data_recoder(place_pos, p2)
    place_information = place_infor(ID, index_ls, place_label_all, place_pos)
    #Test data above

    place_information = place_infor(ID, index_ls, place_label_all, place_pos)
    user1 = users('0000001', time_recoder01, data_recoder01, place_pos)
    suggest = recommend_system(user1.time_recoder, user1.recoder_name).suggest_place

    print("User ID " + str(user1.ID) + " should go to " + place_information.Name[suggest])

"""
    print(place_information.feature)
    print("time_recoder shape is", user1.time_recoder.shape, "data_recoder is", user1.data_recoder.shape,
          "recoder_name shape is", user1.recoder_name.shape)
"""

if __name__ == "__main__":
    main()