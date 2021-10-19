package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Truth extends AnimatorCard_UltraRare
{
    private static final Crystallize status = new Crystallize();

    public static final EYBCardData DATA = Register(Truth.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.Normal)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.FullmetalAlchemist)
            .PostInitialize(data -> data.AddPreview(status, false));

    public Truth()
    {
        super(DATA);

        Initialize(0, 0, 4, 3);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(2);
        SetAffinity_Light(2);
        SetAffinity_Dark(2);

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
            if (GameUtilities.IsCommonBuff(po) || GameUtilities.IsCommonDebuff(po)) {
                playerPowers.add(po);
                GameActions.Bottom.RemovePower(p, po);
            }
        }
        for (AbstractPower po : m.powers) {
            if (GameUtilities.IsCommonBuff(po) || GameUtilities.IsCommonDebuff(po)) {
                enemyPowers.add(po);
                GameActions.Bottom.RemovePower(m, po);
            }
        }

        for (AbstractPower po : playerPowers) {
            po.owner = m;
            GameActions.Bottom.ApplyPower(player,m,po);
        }
        for (AbstractPower po : enemyPowers) {
            po.owner = player;
            GameActions.Bottom.ApplyPower(player,player,po);
        }
    }
}