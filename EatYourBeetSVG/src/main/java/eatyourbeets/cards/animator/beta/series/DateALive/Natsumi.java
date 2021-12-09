package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.animator.beta.curse.Curse_Depression;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.HashMap;

public class Natsumi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Natsumi.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_Depression(), false));

    private static HashMap<Integer, ArrayList<AbstractCard>> cardPool;

    public Natsumi()
    {
        super(DATA);

        Initialize(2, 0, 2, 2);
        SetUpgrade(0,0, 1, 1);
        SetAffinity_Blue(1, 0, 2);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Blue, 6);
        SetAffinityRequirement(Affinity.Light, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.FIRE).forEach(d -> d
        .SetOptions(true, false));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Bottom.ReplaceCard(card.uuid, AbstractDungeon.getCard(CardRarity.UNCOMMON).makeCopy());
            }
        });

        if (player.filledOrbCount() > 0) {
            GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Blue, Affinity.Light).AddConditionalCallback(() -> {
                for (int i = 0; i < Math.min(secondaryValue, player.filledOrbCount()); i++) {
                    AbstractOrb orb = player.orbs.get(0);
                    GameUtilities.ModifyOrbBasePassiveAmount(orb, GameUtilities.GetOrbBasePassiveAmount(orb) * 2, false, false);
                    GameUtilities.ModifyOrbBaseEvokeAmount(orb, GameUtilities.GetOrbBaseEvokeAmount(orb) * 2, false, false);
                    GameActions.Bottom.MakeCardInDrawPile(new Curse_Depression());
                }
            });
        }

    }
}