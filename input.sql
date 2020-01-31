select 
S.S_SAMPLEID ,S.SAMPLESTATUS, SD.S_DATASETSTATUS , SD.DATASET, SDI.PARAMLISTID, SDI.PARAMID, SDI.DISPLAYVALUE
, to_char(S.COLLECTIONDT,'yyyy-mm-dd hh24:mi:ss') collectdate,S.U_CONDITION 
, S.SAMPLETYPEID ,D.U_MDNAME dept, R.U_MDNAME roomcode
, SP.U_MDNAME spcode, ps.u_mdname personcode,TM.U_TESTMETHODID 
, SWI.WORKITEMID, sspec.specid
,v.normalop1 || v.normallimit1 normal
,v.alertop1 || v.alertlimit1 || ' ' || v.alertop2 || v.alertlimit2 alert
,v.actionop1 || v.actionlimit1 action
, SP.U_ACTIONLIMITEXCEEDED, SP.U_SINGLESAMPLEALERTEXE, sspec.condition, s.u_3x, s.u_3xevaluted, s.collectedby, sb.u_batchno
from s_sample s
left join sdidata sd on S.S_SAMPLEID = SD.KEYID1 and SD.SDCID = 'Sample'
left join sdidataitem sdi on S.S_SAMPLEID = SDI.KEYID1 and SDI.SDCID = 'Sample' and SD.PARAMLISTID = SDI.PARAMLISTID and SD.DATASET = SDI.DATASET
left join sdidataapproval sda on S.S_SAMPLEID = SDA.KEYID1 and SDA.SDCID = 'Sample' and SD.PARAMLISTID = SDA.PARAMLISTID and SD.DATASET = SDA.DATASET
left join sdiworkitem swi on SWI.KEYID1 = S.S_SAMPLEID and SWI.SDCID = 'Sample'
left join sdidataitemspec sspec on sspec.sdcid = 'Sample' and sspec.keyid1 = s.s_sampleid and SD.PARAMLISTID = sspec.PARAMLISTID and sdi.paramid = sspec.paramid and sspec.dataset = sd.dataset
join s_samplepoint sp on SP.S_SAMPLEPOINTID = S.SAMPLEPOINTID
join u_room r on R.U_ROOMID = SP.U_ROOMID
join department d on D.DEPARTMENTID = R.U_DEPARTMENTID
left join u_person ps on s.u_personid = ps.u_personid
left join s_batch sb on sb.s_batchid = s.batchid
join u_testmethod tm on TM.U_TESTMETHODID = SP.U_TESTMETHOD
left join sapphire.SPECPARAMLIMITS_V v on v.SPECID = sspec.specid and V.PARAMLISTID = SDI.PARAMLISTID and V.PARAMID = SDI.PARAMID
where  S.COLLECTIONDT between
    to_date('2017-04-13 00:00:00','yyyy-mm-dd hh24:mi:ss') and
    to_date('2017-04-13 00:15:59','yyyy-mm-dd hh24:mi:ss')
order by sp.u_mdname