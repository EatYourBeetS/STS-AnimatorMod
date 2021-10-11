package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.status.SearingBurn;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Midou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Midou.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new SearingBurn(), false));

    public Midou()
    {
        super(DATA);

        Initialize(2, 0, 1, 1);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Fire(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID)) {

            GameActions.Bottom.SelectFromPile(name, magicNumber, player.drawPile, player.hand, player.discardPile)
                    .SetOptions(true, true)
                    .SetFilter(c -> c instanceof Burn)
                    .AddCallback(cards ->
                    {
                        for (AbstractCard card : cards)
                        {
                            GameEffects.Queue.ShowCardBriefly(card);
                            GameActions.Last.ReplaceCard(card.uuid, new SearingBurn());
                        }
                    });
        }

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);

        GameActions.Bottom.ChannelOrb(new Fire());
        GameActions.Bottom.MakeCardInHand(new Burn());
    }
}