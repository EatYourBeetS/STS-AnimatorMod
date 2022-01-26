package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.status.Crystallize;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class Truth extends PCLCard_UltraRare
{
    private static final Crystallize status = new Crystallize();

    public static final PCLCardData DATA = Register(Truth.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.Normal)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.FullmetalAlchemist)
            .PostInitialize(data -> data.AddPreview(status, false));

    public Truth()
    {
        super(DATA);

        Initialize(0, 0, 4, 3);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetPurge(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        ArrayList<AbstractPower> playerPowers = new ArrayList<>();
        ArrayList<AbstractPower> enemyPowers = new ArrayList<>();
        for (AbstractPower po : p.powers) {
            if (PCLGameUtilities.IsCommonBuff(po) || PCLGameUtilities.IsCommonDebuff(po)) {
                playerPowers.add(po);
                PCLActions.Bottom.RemovePower(p, po);
            }
        }
        for (AbstractPower po : m.powers) {
            if (PCLGameUtilities.IsCommonBuff(po) || PCLGameUtilities.IsCommonDebuff(po)) {
                enemyPowers.add(po);
                PCLActions.Bottom.RemovePower(m, po);
            }
        }

        for (AbstractPower po : playerPowers) {
            po.owner = m;
            PCLActions.Bottom.ApplyPower(player,m,po);
        }
        for (AbstractPower po : enemyPowers) {
            po.owner = player;
            PCLActions.Bottom.ApplyPower(player,player,po);
        }
    }
}