package eatyourbeets.cards.animator.series.Overlord;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Sebas extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sebas.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Sebas()
    {
        super(DATA);

        Initialize(0, 6, 3);
        SetUpgrade(0, 3);

        SetAffinity_Red(0, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Orange(1);

        SetExhaust(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block).AddCallback(() ->
        {
            for (EnemyIntent intent : JUtils.Filter(GameUtilities.GetIntents(), i -> i.isAttacking))
            {
                GameActions.Bottom.DealDamage(player, intent.enemy, player.currentBlock, DamageInfo.DamageType.THORNS, AttackEffects.BLUNT_HEAVY);
            }
        });
    }
}