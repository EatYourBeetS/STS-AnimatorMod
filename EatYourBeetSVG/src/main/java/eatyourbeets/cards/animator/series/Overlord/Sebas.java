package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Sebas extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sebas.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Sebas()
    {
        super(DATA);

        Initialize(0, 6, 3);
        SetUpgrade(0, 3);
        SetScaling(0, 1, 2);

        SetExhaust(true);
        SetSeries(CardSeries.Overlord);
        SetAffinity(2, 0, 0, 1, 0);
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
                GameActions.Bottom.DealDamage(player, intent.enemy, player.currentBlock, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            }
        });
    }
}