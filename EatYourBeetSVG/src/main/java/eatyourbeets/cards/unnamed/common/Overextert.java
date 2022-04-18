package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;

public class Overextert extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Overextert.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public Overextert()
    {
        super(DATA);

        Initialize(0, 5, 3);
        SetUpgrade(0, 3, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        for (UnnamedDoll doll : CombatStats.Dolls.GetAll())
        {
            GameActions.Bottom.DealDamage(player, doll, magicNumber, DamageInfo.DamageType.NORMAL, AttackEffects.ICE)
            .SetVFXColor(Colors.Violet(1))
            .SetSoundPitch(0.9f, 0.9f)
            .SetDuration(0.15f, true);
        }
    }
}