package eatyourbeets.cards.animator.beta.series.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAddedToDrawPileSubscriber;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class MukuroHoshimiya extends AnimatorCard implements StartupCard, OnShuffleSubscriber, OnAddedToDrawPileSubscriber
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

        GameActions.Bottom.DealDamage(this, m, AttackEffects.PSYCHOKINESIS);
        GameActions.Bottom.Scry(secondaryValue).AddCallback(cards -> {
            cardList.addAll(cards);
        });
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

        CombatStats.onShuffle.Subscribe(this);
    }

    @Override
    public void OnAddedToDrawPile(boolean visualOnly, CardSelection.Mode destination)
    {
        OnShuffle(false);
    }

    @Override
    public void OnShuffle(boolean triggerRelics)
    {
        for (int i = 0; i < cardList.size(); i++) {
            int index = i;
            GameActions.Top.Callback(() -> JUtils.ChangeIndex(cardList.get(index), player.drawPile.group, index));
        }
        cardList.clear();
    }
}