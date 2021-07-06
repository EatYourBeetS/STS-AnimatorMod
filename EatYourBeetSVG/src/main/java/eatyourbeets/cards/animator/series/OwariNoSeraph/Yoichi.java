package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Yoichi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yoichi.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSynergy(Synergies.OwariNoSeraph);

    public Yoichi()
    {
        super(DATA);

        Initialize(0,0, 2);
        SetUpgrade(0,2, 0);

        //TODO: Affinity upgrade and visual effect
        //SetAffinity_Green(1, 0);
        //SetAffinity_Light(1, 1);

        SetAffinity(0, 1, 0, 1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        AddAffinity(0, 0, 0, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false);
        GameActions.Bottom.StackPower(new SupportDamagePower(p, 1))
        .AddCallback(power ->
        {
            if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
            {
                SupportDamagePower supportDamage = JUtils.SafeCast(power, SupportDamagePower.class);
                if (supportDamage != null)
                {
                    supportDamage.atEndOfTurn(true);
                }
            }
        });
    }
}