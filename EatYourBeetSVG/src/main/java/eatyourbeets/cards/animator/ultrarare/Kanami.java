package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Kanami extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Kanami.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new KanamiAlt(), true);
    }

    public Kanami()
    {
        super(DATA);

        Initialize(20, 0, 2);
        SetUpgrade(7, 0, 0);

        SetCooldown(2, 0, this::OnCooldownCompleted);
        SetHaste(true);
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.LogHorizon);
        SetAffinity(1, 2, 0, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
        .SetVFX(false, true)
        .AddCallback(enemies ->
        {
            CardCrawlGame.sound.play("ATTACK_WHIRLWIND");
            for (AbstractCreature c : enemies)
            {
                GameActions.Top.VFX(new WhirlwindEffect(), 0);
                GameEffects.List.Add(new WhirlwindEffect());
                GameActions.Bottom.ApplyVulnerable(player, c, magicNumber)
                .ShowEffect(false, true);
            }
        });
        GameActions.Last.MoveCard(this, p.drawPile)
        .ShowEffect(true, true)
        .SetDestination(CardSelection.Random)
        .AddCallback(() -> cooldown.ProgressCooldownAndTrigger(null));
    }

    private void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Last.ReplaceCard(uuid, new KanamiAlt())
        .SetUpgrade(upgraded)
        .AddCallback(cardMap ->
        {
            for (AbstractCard key : cardMap.keySet())
            {
                KanamiAlt other = (KanamiAlt) cardMap.get(key);
                other.intellectScaling = this.intellectScaling;
                other.agilityScaling = this.agilityScaling;
                other.forceScaling = this.forceScaling;
            }
        });
    }
}