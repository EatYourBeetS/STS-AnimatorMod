package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.animator.series.MadokaMagica.SayakaMiki;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;

public class SayakaMiki_Oktavia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SayakaMiki_Oktavia.class)
            .SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeries(SayakaMiki.DATA.Series);

    public SayakaMiki_Oktavia()
    {
        super(DATA);

        Initialize(16, 8, 5, 5);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Dark, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(VFX.Mindblast(p.dialogX, p.dialogY).SetColor(Color.VIOLET));
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.DARK);
        GameActions.Bottom.StackPower(new EquilibriumPower(p, 1));
        GameActions.Bottom.DrawReduction(secondaryValue);
        GameActions.Bottom.RecoverHP(magicNumber);

        if (TryUseAffinity(Affinity.Dark))
        {
            GameActions.Bottom.FetchFromPile(name, 999, p.drawPile, p.discardPile)
            .SetOptions(true, true)
            .SetFilter(c -> Curse_GriefSeed.DATA.ID.equals(c.cardID));
        }
    }
}
