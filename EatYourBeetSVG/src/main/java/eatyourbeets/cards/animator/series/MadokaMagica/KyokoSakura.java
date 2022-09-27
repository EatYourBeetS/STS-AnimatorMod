package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.animator.special.KyokoSakura_Ophelia;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class KyokoSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KyokoSakura.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing, EYBCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.MadokaMagica_Witch(KyokoSakura_Ophelia.DATA));
                data.AddPreview(new KyokoSakura_Ophelia(), true);
                data.AddPreview(new Curse_GriefSeed(), false);
            });

    public KyokoSakura()
    {
        super(DATA);

        Initialize(11, 0, 1);
        SetUpgrade(3, 0);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Blue(1);

        SetAffinityRequirement(Affinity.Red, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SPEAR).SetVFXColor(Color.RED);
        GameActions.Bottom.Draw(magicNumber);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.ChannelOrb(new Fire());
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(cards ->
        {
            for (int i = cards.size() - 1; i >= 0; i--)
            {
                GameActions.Top.MoveCard(cards.get(i), player.hand, player.drawPile)
                .SetDestination(CardSelection.Top);
            }
        });
    }
}
