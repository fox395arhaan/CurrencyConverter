package com.currencyapp.currencyconverter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by raghav on 12/1/16.
 */
public class Country {

    public String shortName;
    public String fullName;
    public int imageId;
    public boolean isSelected;

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public Country() {
    }

    public Country(String shortName, String fullName,  boolean isSelected) {
        this.shortName = shortName;
        this.fullName = fullName;

        this.isSelected = isSelected;
    }

    @Override
    public boolean equals(Object obj) {

        boolean result = false;

        if (obj == null) {
            return result;
        }

        if (obj instanceof Country) {

            Country country = (Country) obj;
            if (country.fullName.equalsIgnoreCase(this.fullName) && country.shortName.equalsIgnoreCase(this.shortName)) {
                result = true;
            }

        } else {
            return result;
        }


        return result;

    }

    // just omitted null checks
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 7 * hash + this.shortName.hashCode();
        hash = 7 * hash + this.fullName.hashCode();
        return hash;
    }


    public static ArrayList<Country> getAllCountries(Context context) {


        String[] defaultName = new String[]{"USD", "INR", "EUR", "GBP", "CAD", "AUD"};
        List<String> strings = Arrays.asList(defaultName);
        ArrayList<Country> countries = new ArrayList<>();
        String[] FullNamelist = context.getResources().getStringArray(R.array.conturiesfullName);
        String[] ShortNamelist = context.getResources().getStringArray(R.array.shortName);

        int[] ints = new int[]{
                R.drawable.flag_usd,
                R.drawable.flag_inr,
                R.drawable.flag_eur,
                R.drawable.flag_gbp,
                R.drawable.flag_cad,
                R.drawable.flag_aud,
                R.drawable.flag_afn,
                R.drawable.flag_all,
                R.drawable.flag_dzd,
                R.drawable.flag_aoa,
                R.drawable.flag_ars,
                R.drawable.flag_amd,
                R.drawable.flag_awg,
                R.drawable.flag_ats,
                R.drawable.flag_azn,
                R.drawable.flag_bsd,
                R.drawable.flag_bhd,
                R.drawable.flag_bdt,
                R.drawable.flag_bbd,
                R.drawable.flag_byr,
                R.drawable.flag_bef,
                R.drawable.flag_bzd,
                R.drawable.flag_bmd,
                R.drawable.flag_btn,
                R.drawable.flag_btc,
                R.drawable.flag_bob,
                R.drawable.flag_bam,
                R.drawable.flag_bwp,
                R.drawable.flag_brl,
                R.drawable.flag_bnd,
                R.drawable.flag_bgn,
                R.drawable.flag_bif,
                R.drawable.flag_khr,
                R.drawable.flag_cve,
                R.drawable.flag_kyd,
                R.drawable.flag_xof,
                R.drawable.flag_xaf,
                R.drawable.flag_xpf,
                R.drawable.flag_clp,
                R.drawable.flag_cny,
                R.drawable.flag_cop,
                R.drawable.flag_kmf,
                R.drawable.flag_cdf,
                R.drawable.flag_crc,
                R.drawable.flag_hrk,
                R.drawable.flag_cup,
                R.drawable.flag_cyp,
                R.drawable.flag_czk,
                R.drawable.flag_dkk,
                R.drawable.flag_djf,
                R.drawable.flag_dop,
                R.drawable.flag_nlg,
                R.drawable.flag_xcd,
                R.drawable.flag_egp,
                R.drawable.flag_svc,
                R.drawable.flag_ern,
                R.drawable.flag_eek,
                R.drawable.flag_etb,
                R.drawable.flag_fkp,
                R.drawable.flag_fjd,
                R.drawable.flag_fim,
                R.drawable.flag_frf,
                R.drawable.flag_gmd,
                R.drawable.flag_gel,
                R.drawable.flag_dem,
                R.drawable.flag_ghs,
                R.drawable.flag_gip,
                R.drawable.flag_xau,
                R.drawable.flag_grd,
                R.drawable.flag_gtq,
                R.drawable.flag_gnf,
                R.drawable.flag_gyd,
                R.drawable.flag_htg,
                R.drawable.flag_hnl,
                R.drawable.flag_hkd,
                R.drawable.flag_huf,
                R.drawable.flag_isk,
                R.drawable.flag_idr,
                R.drawable.flag_irr,
                R.drawable.flag_iqd,
                R.drawable.flag_iep,
                R.drawable.flag_ils,
                R.drawable.flag_itl,
                R.drawable.flag_jmd,
                R.drawable.flag_jpy,
                R.drawable.flag_jod,
                R.drawable.flag_kzt,
                R.drawable.flag_kes,
                R.drawable.flag_kwd,
                R.drawable.flag_kgs,
                R.drawable.flag_lak,
                R.drawable.flag_lvl,
                R.drawable.flag_lbp,
                R.drawable.flag_lsl,
                R.drawable.flag_lrd,
                R.drawable.flag_lyd,
                R.drawable.flag_ltl,
                R.drawable.flag_luf,
                R.drawable.flag_mop,
                R.drawable.flag_mkd,
                R.drawable.flag_mga,
                R.drawable.flag_mwk,
                R.drawable.flag_myr,
                R.drawable.flag_mvr,
                R.drawable.flag_mtl,
                R.drawable.flag_mro,
                R.drawable.flag_mur,
                R.drawable.flag_mxn,
                R.drawable.flag_mdl,
                R.drawable.flag_mnt,
                R.drawable.flag_mad,
                R.drawable.flag_mzn,
                R.drawable.flag_mmk,
                R.drawable.flag_nad,
                R.drawable.flag_npr,
                R.drawable.flag_nzd,
                R.drawable.flag_nio,
                R.drawable.flag_ngn,
                R.drawable.flag_ang,
                R.drawable.flag_kpw,
                R.drawable.flag_nok,
                R.drawable.flag_omr,
                R.drawable.flag_pkr,
                R.drawable.flag_xpd,
                R.drawable.flag_pab,
                R.drawable.flag_pgk,
                R.drawable.flag_pyg,
                R.drawable.flag_pen,
                R.drawable.flag_php,
                R.drawable.flag_xpt,
                R.drawable.flag_pln,
                R.drawable.flag_pte,
                R.drawable.flag_qar,
                R.drawable.flag_ron,
                R.drawable.flag_rub,
                R.drawable.flag_rwf,
                R.drawable.flag_wst,
                R.drawable.flag_std,
                R.drawable.flag_sar,
                R.drawable.flag_rsd,
                R.drawable.flag_scr,
                R.drawable.flag_sll,
                R.drawable.flag_xag,
                R.drawable.flag_sgd,
                R.drawable.flag_skk,
                R.drawable.flag_sit,
                R.drawable.flag_sbd,
                R.drawable.flag_sos,
                R.drawable.flag_zar,
                R.drawable.flag_krw,
                R.drawable.flag_esp,
                R.drawable.flag_lkr,
                R.drawable.flag_shp,
                R.drawable.flag_sdg,
                R.drawable.flag_srd,
                R.drawable.flag_szl,
                R.drawable.flag_sek,
                R.drawable.flag_chf,
                R.drawable.flag_syp,
                R.drawable.flag_twd,
                R.drawable.flag_tjs,
                R.drawable.flag_thb,
                R.drawable.flag_top,
                R.drawable.flag_ttd,
                R.drawable.flag_tnd,
                R.drawable.flag_try,
                R.drawable.flag_tmt,
                R.drawable.flag_ugx,
                R.drawable.flag_uah,
                R.drawable.flag_uyu,
                R.drawable.flag_aed,
                R.drawable.flag_uzs,
                R.drawable.flag_vuv,
                R.drawable.flag_vef,
                R.drawable.flag_vnd,
                R.drawable.flag_yer,
                R.drawable.flag_zmw,

        };

        for (int i = 0; i < ShortNamelist.length; i++) {

            boolean isSelected = false;

            if (strings.contains(ShortNamelist[i])) {
                isSelected = true;
            }
            Country country = new Country(ShortNamelist[i], FullNamelist[i], isSelected);
            countries.add(country);
        }
        return countries;

    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
