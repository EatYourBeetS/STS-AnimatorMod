package eatyourbeets.cards.animator.beta.series.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class MukuroHoshimiya extends AnimatorCard implements StartupCard, OnShuffleSubscriber
{
    public static final EYBCardData DATA = Register(MukuroHoshimiya.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental).SetSeriesFromClassPackage();
    private final ArrayList<AbstractCard> cardList = new ArrayList<>();


    public MukuroHoshimiya()
    {
        super(DATA);

        Initialize(14, 0, 4, 3);
        SetUpgrade(0,0,-1);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(1, 0, 2);
        SetAffinity_Silver(1,0,0);
        SetAffinity_Dark(0,0,1);
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage + (player.drawPile.size() / magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.PSYCHOKINESIS).forEach(d -> d.AddCallback(() ->
                GameActions.Bottom.Scry(secondaryValue).AddCallback(cards ->
                        {
                            for (AbstractCard card : cards) {
                                if (!card.hasTag(DELAYED)) {
                                    cardList.add(card);
                                    card.tags.add(DELAYED);
                                }
                            }
                            if (cardList.size() > 0) {
                                CombatStats.onShuffle.Subscribe(this);
                            }
                        }
                )).SetDuration(0.5f,false));

    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        OnShuffle(false);

        return false;
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
    }

    @Override
    public void OnShuffle(boolean triggerRelics)
    {
        GameActions.Top.Callback(() -> {
            for (AbstractCard card : cardList) {
                if (card != null) {
                    card.tags.remove(DELAYED);
                }

            }
            cardList.clear();
            CombatStats.onShuffle.Unsubscribe(this);
        });
    }
}