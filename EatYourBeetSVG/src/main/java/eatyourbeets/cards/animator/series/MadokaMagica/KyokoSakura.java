package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.KyokoSakura_Ophelia;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class KyokoSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KyokoSakura.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new KyokoSakura_Ophelia(), true);
            });

    public KyokoSakura()
    {
        super(DATA);

        Initialize(9, 0, 1, 2);
        SetUpgrade(3, 0, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(0,0,1);
        SetSoul(4, 0, KyokoSakura_Ophelia::new);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.ApplyBurning(TargetHelper.Normal(m), secondaryValue);

        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(cards ->
        {
            for (int i = cards.size() - 1; i >= 0; i--)
            {
                GameActions.Top.MoveCard(cards.get(i), player.hand, player.drawPile)
                .SetDestination(CardSelection.Top);
            }
        });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}
