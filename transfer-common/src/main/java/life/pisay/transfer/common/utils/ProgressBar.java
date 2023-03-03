package life.pisay.transfer.common.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author Viices Cai
 * @time 2022/6/21
 */
public class ProgressBar {

    private int index = 0;
    private String finish;
    private String unFinish;


    // 进度条粒度
    private final int PROGRESS_SIZE = 50;
    private int BITE = 2;

    private String getNChar(int num, char ch){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < num; i++){
            builder.append(ch);
        }
        return builder.toString();
    }

    public void printProgress() throws InterruptedException {
        System.out.print("Progress:");

        finish = getNChar(index / BITE, '■');
        unFinish = getNChar(PROGRESS_SIZE - index / BITE, ' ');
        String target = String.format("%3d%%[%s%s]", index, finish, unFinish);
        System.out.print(target);

        while (index <= 100){
            finish = getNChar(index / BITE, '■');
            unFinish = getNChar(PROGRESS_SIZE - index / BITE, ' ');

            target = String.format("%3d%%[%s%s]", index, finish, unFinish);
            System.out.print(getNChar(PROGRESS_SIZE + 6, '\b'));
            System.out.print(target);

            TimeUnit.MILLISECONDS.sleep(100);
            index++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ProgressBar progressBar = new ProgressBar();
        progressBar.printProgress();
    }
}
