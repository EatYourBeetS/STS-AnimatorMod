package eatyourbeets.cards.animator.beta.series.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

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
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage + (player.drawPile.size() / magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

        GameActions.Bottom.DealDamage(this, m, AttackEffects.PSYCHOKINESIS).AddCallback(() ->
                GameActions.Bottom.Scry(secondaryValue).AddCallback(cards ->
                        {
                            if (cards.size() > 0) {
                                cardList.addAll(cards);
                                CombatStats.onShuffle.Subscribe(this);
                            }
                        }
                )).SetDuration(0.5f,true);

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
            for (int i = 0; i < cardList.size(); i++) {
                AbstractCard card = cardList.get(i);
                if (card != null) {
                    GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
                    JUtils.ChangeIndex(cardList.get(i), player.drawPile.group, i);
                }

            }
            cardList.clear();
            CombatStats.onShuffle.Unsubscribe(this);
        });
    }
}