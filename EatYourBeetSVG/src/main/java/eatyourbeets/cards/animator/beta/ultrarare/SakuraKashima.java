package eatyourbeets.cards.animator.beta.ultrarare;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import eatyourbeets.cards.animator.beta.special.Miracle;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class SakuraKashima extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(SakuraKashima.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Miracle(), false);
    }

    private static final CardEffectChoice choices = new CardEffectChoice();

    public SakuraKashima()
    {
        super(DATA);

        Initialize(0, 0, 10);
        SetUpgrade(0, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.Rewrite);
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.GainIntellect(1, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        String[] text = DATA.Strings.EXTENDED_DESCRIPTION;

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(text[0], this::Effect1);
            choices.AddEffect(text[1], this::Effect2);
        }

        choices.Select(1, m);
    }

    private void Effect1(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.MakeCardInHand(new Miracle());
    }

    private void Effect2(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        int numMiraclesToMake = BaseMod.MAX_HAND_SIZE - player.hand.size();

        for (int i=0; i<numMiraclesToMake; i++)
        {
            GameActions.Bottom.MakeCardInHand(new Miracle());
        }

        GameActions.Bottom.ApplyPower(new EndTurnDeathPower(p));
    }
}