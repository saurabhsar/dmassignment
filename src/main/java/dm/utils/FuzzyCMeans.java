package dm.utils;

import weka.core.Instances;

import java.util.Random;

/**
 *
 */
public class FuzzyCMeans implements UtilInterface {

    private final Integer MAX_DATA_POINTS = 10000;
    private final Integer MAX_CLUSTER = 2;
    private final Integer MAX_DATA_DIMENSION = 23;


    private int num_data_points;
    private int num_clusters = 2;
    private int num_dimensions = 23;
    private Double[][] low_high = new Double[MAX_DATA_DIMENSION][23];
    private Double[][] degree_of_memb = new Double[MAX_DATA_POINTS][MAX_CLUSTER];
    private double epsilon = 0.000500;
    private double fuzziness = 3.00;
    private Double[][] data_point = new Double[MAX_DATA_POINTS][MAX_DATA_DIMENSION];
    private Double[][] cluster_centre = new Double[MAX_CLUSTER][MAX_DATA_DIMENSION];

    private void initialize_arrays(){
        for (int i = 0; i < MAX_DATA_DIMENSION ; i++) {
            for (int j = 0; j < 23; j++) {
                low_high[i][j] = (double) 0;
            }

            for (int j = 0; j < MAX_CLUSTER; j++) {
                cluster_centre[j][i] = (double) 0;
            }
        }

        for (int i = 0; i < MAX_DATA_POINTS; i++) {
            for (int j = 0; j < MAX_DATA_DIMENSION; j++) {
                data_point[i][j] = (double) 0;
            }
            for(int j = 0; j < MAX_CLUSTER; j++) {
                degree_of_memb[i][j] = (double) 0;
            }
        }
    }

    private void init(Instances instances) {

        num_data_points = instances.size();

        int i, j, r, rval;
        double s;
        for (i = 0; i < num_data_points; i++) {
            for (j = 1; j < num_dimensions; j++) {
                data_point[i][j] = instances.get(i).value(j);
                if (data_point[i][j] < low_high[j][0])
                    low_high[j][0] = data_point[i][j];
                if (data_point[i][j] > low_high[j][1])
                    low_high[j][1] = data_point[i][j];
            }
        }
        for (i = 0; i < num_data_points; i++) {
            s = 0.0;
            r = 100;
            for (j = 1; j < num_clusters; j++) {
                rval = (new Random()).nextInt() % (r + 1);
                r -= rval;
                degree_of_memb[i][j] = rval / 100.0;
                s += degree_of_memb[i][j];
            }
            degree_of_memb[i][0] = 1.0 - s;
        }
    }

    private void calculate_centre_vectors() {
        int i, j, k;
        double numerator, denominator;
        Double[][] t = new Double[MAX_DATA_POINTS][MAX_CLUSTER];
        for (i = 0; i < num_data_points; i++) {
            for (j = 0; j < num_clusters; j++) {
                t[i][j] = Math.pow(degree_of_memb[i][j], fuzziness);
            }
        }
        for (j = 0; j < num_clusters; j++) {
            for (k = 0; k < num_dimensions; k++) {
                numerator = 0.0;
                denominator = 0.0;
                for (i = 0; i < num_data_points; i++) {
                    numerator += t[i][j] * data_point[i][k];
                    denominator += t[i][j];
                }
                cluster_centre[j][k] = numerator / denominator;
            }
        }
    }

    private double get_norm(int i, int j) {
        int k;
        double sum = 0.0;
        for (k = 0; k < num_dimensions; k++) {
            sum += Math.pow(data_point[i][k] - cluster_centre[j][k], 2);
        }
        return Math.sqrt(sum);
    }

    private double get_new_value(int i, int j) {
        int k;
        double t, p, sum;
        sum = 0.0;
        p = 2 / (fuzziness - 1);
        for (k = 0; k < num_clusters; k++) {
            t = get_norm(i, j) / get_norm(i, k);
            t = Math.pow(t, p);
            sum += t;
        }
        return 1.0 / sum;
    }

    private double update_degree_of_membership() {
        int i, j;
        double new_uij;
        double max_diff = 0.0, diff;
        for (j = 0; j < num_clusters; j++) {
            for (i = 0; i < num_data_points; i++) {
                new_uij = get_new_value(i, j);
                diff = new_uij - degree_of_memb[i][j];
                if (diff > max_diff)
                    max_diff = diff;
                degree_of_memb[i][j] = new_uij;
            }
        }
        return max_diff;
    }

    private void fcm(Instances instances) {
        initialize_arrays();
        double max_diff;
        init(instances);
        do {
            calculate_centre_vectors();
            max_diff = update_degree_of_membership();
        } while (max_diff > epsilon);

        System.out.println("test");
    }


    @Override
    public void train(Instances instances) throws Exception {
        fcm(instances);
    }
}
